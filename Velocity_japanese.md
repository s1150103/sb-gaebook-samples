# Velocityによる日本語テンプレートのレンダリング #

Appendix Gに示したRedererでは、テンプレート内で日本語を用いると文字化けします。これは、テンプレートの文字コードが指定されていないためです。下記のように Renderer#initializeVelocityメソッド内でINPUT\_ENCODINGプロパティを設定してください。

```
private static void initializeVelocity() throws Exception {
    Velocity.setProperty( Velocity.RUNTIME_LOG_LOGSYSTEM, new JdkLogChute());
    Velocity.setProperty( Velocity.INPUT_ENCODING, "UTF8");
    Velocity.init();
    initialized = true;
}
```

なお、Blogアプリなどのソースツリー上のRederer.javaはこのプロパティを含んでいますので、そのままお使いいただけます。