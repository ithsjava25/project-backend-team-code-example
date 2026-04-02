@import demo.codeexample.comment.CreateCommentDto
@param CreateCommentDto comment

<!DOCTYPE html>
<html>
<head>
    <title>Write a Comment</title>
</head>
<body>
<h1>Write a Comment</h1>

<form action="/comments" method="post">
<%--    <label for="postId">Post ID:</label>--%>
<%--    <input type="number" id="postId" name="postId" value="${comment.postId}" />--%>

<%--    <label for="authorId">Author ID:</label>--%>
<%--    <input type="number" id="authorId" name="authorId" value="${comment.authorId}" />--%>

    <label for="content">Content:</label>
    <textarea id="content" name="content">${comment.getContent()}</textarea>

    <button type="submit">Submit</button>
</form>
</body>
</html>
