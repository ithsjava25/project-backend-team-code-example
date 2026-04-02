@import demo.codeexample.comment.CommentDto
@import java.util.List

@param List<CommentDto> comments

<!DOCTYPE html>
<html>
<head>
    <title>Comments</title>
</head>
<body>
<h1>All Comments</h1>

<ul>
    @for(CommentDto c : comments)
        <li>
            <strong>${c.getCreatedAt().toString()}</strong>
            <span>: ${c.getContent()}</span>
        </li>
    @endfor
</ul>

<a href="/comments/new">Write a new comment</a>
</body>
</html>
