# コードのチェックアウト #
Subclipseを用いたコードのチェックアウト方法を説明します。チェックアウト自体は普通なのですが、その後App Engineのプロジェクトとして利用するためには、ちょっとした後処理が必要です。

## 事前準備 ##
事前に、App Engineのプラグインを入れておいてください。App Engineプラグインの導入に関しては本書の付録をご覧ください。

## レポジトリの登録 ##
まず、このサイトのレポジトリをsubclipseに登録します。

[ウィンドウ][パースペクティブを開く][その他] [SVNリポジトリエクスプローラ]
からSVNレポジトリエクスプローラを開きます。

![http://sb-gaebook-samples.googlecode.com/files/Untitled%2010.png](http://sb-gaebook-samples.googlecode.com/files/Untitled%2010.png)

左側のSVNレポジトリパネルで右クリック、[新規]→[レポジトリー・ロケーション]とします。

![http://sb-gaebook-samples.googlecode.com/files/Screen%20shot%202010-06-12%20at%205.22.00%20PM.png](http://sb-gaebook-samples.googlecode.com/files/Screen%20shot%202010-06-12%20at%205.22.00%20PM.png)

すると下のパネルが開きます。ここに本サイトのレポジトリURLである

> http://sb-gaebook-samples.googlecode.com/svn/trunk

を登録します。

![http://sb-gaebook-samples.googlecode.com/files/Screen%20shot%202010-06-12%20at%205.23.35%20PM.png](http://sb-gaebook-samples.googlecode.com/files/Screen%20shot%202010-06-12%20at%205.23.35%20PM.png)


## チェックアウト ##

Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages


## 後処理 ##
チェックアウトした状態ではApp Engineプロジェクトとして正常に動きません。これは、svnにはApp Engineに必要なjarが含まれていないからです。この問題を解決するには、プロジェクトで一度App Engineの使用をやめ、再度App Engineを使用するようにすればＯＫです。

各プロジェクトにApp Engine関連のjarを含めなかったのは、サイズが大きくなりすぎてしまうからです。また、App Engine のjarはSDKがバージョンアップすると変更されるので、レポジトリに入れておいても仕方がないということもあります。

App Engineの利用を制御するには、プロジェクトの上で右クリックをして、コンテクストメニューを出し、[Google](Google.md)を選択します。すると下のようなパネルが開くはずです。ここで、上の[Google App Engine](Use.md) チェックボックスを外して、[OK](OK.md)をクリックして一度パネルを閉じます。

次に同じ手順で再度パネルを開き、[Google App Engine](Use.md)チェックボックスを、今度はチェックしてパネルを閉じます。これでjarが配備されたはずです。プロジェクト/war/lib 以下を開いて確認してみてください。

![http://sb-gaebook-samples.googlecode.com/files/Screen%20shot%202010-06-12%20at%206.52.59%20PM-mod.png](http://sb-gaebook-samples.googlecode.com/files/Screen%20shot%202010-06-12%20at%206.52.59%20PM-mod.png)


## 注意 ##
チェックアウトした際にコード内のコメントが文字化けしている場合は、ワークスペースの文字コードの設定がUTF-8になっているかを確認してみてください。

[ウィンドウ]→[設定]でパネルを開き、[一般]→[ワークスペース]の[テキスト・ファイル・エンコード]をUTF-8にしてください。

![http://sb-gaebook-samples.googlecode.com/files/Screen%20shot%202010-06-13%20at%201.24.50%20AM-mod2.png](http://sb-gaebook-samples.googlecode.com/files/Screen%20shot%202010-06-13%20at%201.24.50%20AM-mod2.png)