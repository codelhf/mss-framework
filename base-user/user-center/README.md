## description
1. token 放在请求头中,兼容app无session
2. 兼容jwt模式token, 可以减少服务器请求压力(本系统中并未实际使用)
3. 第三方认证服务采用oauth2.0协议的方式(自己作为第三方)
4. 提供第三方登录(QQ、微信、微博)等(使用别的第三方)
5. SSO认证也采用oauth2.0协议的简化方式
6. SSO应用使用单独的登录注册页面, 主登录页为oauth2.0认证服务页面(采用扫码方式登录)