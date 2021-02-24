package my.yf;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  generate Update json in output directory.
 */
@Mojo( name = "generate",defaultPhase=LifecyclePhase.PACKAGE) // (since Maven 3.0)
public class MyMojo
        extends AbstractMojo
{

    @Parameter( name = "key")
    private String key;
    @Parameter( name = "needEncrypt",defaultValue = "false")
    private String needEncrypt;
    @Parameter( name = "baseUrl",defaultValue = "")
    private String baseUrl;
    @Parameter( defaultValue = "${project.version}", readonly = true )
    private String version;
    @Parameter( defaultValue = "${project.build.directory}", readonly = true )
    private File target;
    @Parameter( defaultValue = "${project.artifactId}", readonly = true )
    private String targetName;
    @Parameter( defaultValue = "${project.build.finalName}", readonly = true )
    private String finalName;
    public void execute()
    {
        File[] files=target.listFiles();
        File jarFile = null;
        for(File f:files){
            if(f.getName().equals(finalName+".jar")){
                jarFile=f;
                break;
            }
        }
        if(jarFile==null){
            getLog().error("jar文件不存在");
            return;
        }
        String sha1=getSha1(jarFile);
        UpdataJson json=new UpdataJson();
        json.setVersion(version);
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        json.setPublishTime(df.format(new Date()));
        json.setSha1(sha1);
        String packageName=jarFile.getName();

        if(key!=null&&key.length()!=0&&"true".equals(needEncrypt)){
            getLog().info("开始加密打包文件");
            try {
                String encryptName=target.getAbsolutePath()+File.separator+"packages";
                EncFileUtil.encrypt(jarFile,encryptName,key);
                packageName="packages";
				File encrypFile=new File(encryptName);
                long fileSize = encrypFile.length();
                json.setFileSize(fileSize);
            } catch (Exception e) {
                getLog().error("加密打包文件出错");
                e.printStackTrace();
                return;
            }
        }
        json.setPackageUrl(baseUrl+"/"+packageName);
        creatJsonFile(json);
    }

    private String getSha1( File file){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            String hex = DigestUtils.sha1Hex(fileInputStream);
            return hex;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void creatJsonFile(UpdataJson json){
        if(json!=null){
            File f=new File(target.getAbsolutePath()+File.separator+"latest.json");
            try {
                FileOutputStream fos=new FileOutputStream(f);
                BufferedOutputStream bos=new BufferedOutputStream(fos);
                String content= JSON.toJSONString(json);
                bos.write(content.getBytes(),0,content.getBytes().length);
                bos.flush();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
