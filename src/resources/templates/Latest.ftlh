<html lang="en">
<head>
    <title>Latest</title>

   <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
   <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
   <script>hljs.initHighlightingOnLoad();</script>

</head>
<body>
    <table>
        <#list snpts as codesnippet>
            <tr>
                <td><span>${codesnippet.date} </span></td>
            </tr>
            <tr>
                <td><pre><code>${codesnippet.code} </code></pre></td>
            </tr>
        </#list>
    </table>
</body>
</html>