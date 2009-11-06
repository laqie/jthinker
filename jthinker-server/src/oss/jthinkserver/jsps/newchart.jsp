<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="oss.jthinkserver.storage.Storage" %> 
<%@ page import="oss.jthinkserver.storage.UploadSession" %> 

<% String sessionId = request.getParameter("session"); %>
<% UploadSession uploadSession = Storage.lookupUploadSession(sessionId); %>

<html>
  <head>
    <title>jThinker Server</title>
  </head>

  <body>
    <span style="font-family:Helvetica">
    <div align="right">jThinker Server</div> 
    <div align="center">Your chart is successfully published!</div>
    <img src= 
      <%= "\"/newchart?type=image&session=" + sessionId + "\"" %>
    />
    <pre>
      <%= uploadSession.getXmlContent() %>
    </pre>
  </body>
</html>

