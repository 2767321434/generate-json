# generate-json
maven plugin<br>
generate Update json and encrypt jar in output directory<br>
maven 插件<br>
生成自动更新用的json文件和加密（可选）生成的jar文件。<br>
maven install 后如下引入，key是加密的密码，needEncrypt 为true开启加密，baseUrl 是发布后的更新下载地址<br>
```xml
 <plugin>
       <groupId>my.yf</groupId>
       <artifactId>generate-json</artifactId>
       <version>1.0-SNAPSHOT</version><br>
       <executions>
            <execution>
                <phase>package</phase>
                   <goals>
                       <goal>generate</goal>
                    </goals>
             </execution>
        </executions>
        <configuration>
             <key>123456</key>
             <needEncrypt>true</needEncrypt>
             <baseUrl>http://xxx.xxx/update</baseUrl>
         </configuration>
</plugin>
```
