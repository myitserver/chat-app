<!DOCTYPE html>
<html>
<head>
  <title>Java WebSocket Chat</title>
  <script src="/javax.websocket-api-1.1.jar"></script>
  <script src="/jquery-3.6.0.min.js"></script>
  <script>
    $(function() {
      var websocket = new WebSocket("ws://" + location.hostname + ":8080/myapp/chat");

      websocket.onopen = function(event) {
        var username = prompt("Please enter your name:");
        var message = {
          type: "join",
          username: username
        };
        websocket.send(JSON.stringify(message));
      };

      websocket.onmessage = function(event) {
        var data = JSON.parse(event.data);
        if (data.type == "message") {
          var $message = $("<div>").addClass("message");
          var $username = $("<span>").addClass("username").text(data.username + ": ");
          var $text = $("<span>").addClass("text").text(data.text);
          $message.append($username, $text);
          $("#messages").append($message);
        }
      };

      $("#send").click(function() {
        var $input = $("#input");
        var text = $input.val();
        var message = {
          type: "message",
          username: websocket.username,
          text

