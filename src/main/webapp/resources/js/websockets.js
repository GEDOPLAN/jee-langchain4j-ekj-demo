let currentMessage;
let rawMessageBuffer;

function onUserMessage() {
  let message = document.getElementById('ai-chat-form:chat-input-text').value.trim();
  if (message) {
    console.log("Sending message: " + message);
    newMessage(message, true);
    document.getElementById('ai-chat-form:chat-input-text').disabled = true;
    document.getElementById('ai-chat-form:chat-input-text').value = '';
    document.getElementById('ai-chat-form:send-btn').disabled = true;
  }
}

function newMessage(message, user) {
  const chatHistory = document.getElementById('chat-history');
  if (message === "---message-start---" || user) {
    currentMessage = document.createElement('div');
    currentMessage.classList.add('message');

    if (user) {
      currentMessage.classList.add('right');
      currentMessage.textContent = message;
    } else {
      currentMessage.classList.add('left');
      rawMessageBuffer = ""
    }
    // Append to chat history
    chatHistory.appendChild(currentMessage);
  } else if (message === "---message-end---") {
    currentMessage = null;
    rawMessageBuffer = null;
  } else {
    rawMessageBuffer += message;
    currentMessage.innerHTML = DOMPurify.sanitize(marked.parse(rawMessageBuffer));

    // Highlight new blocks
    document.querySelectorAll('pre code').forEach((block) => {
      if (!block.hasAttribute('data-highlighted')) {
        hljs.highlightElement(block);
      }
    });
  }
  // Scroll to the bottom of chat
  chatHistory.scrollTop = chatHistory.scrollHeight;
}


// Faces Callback

function initFaces() {
  document.getElementById('ai-chat-form:chat-input-text').addEventListener('keydown', function (event) {
    if (!event.shiftKey && event.key === "Enter") {
      PrimeFaces.ab({
        s: "ai-chat-form:send-btn",
        f: "ai-chat-form"
      });
      onUserMessage();
      event.preventDefault();
    }
  });
  document.getElementById('ai-chat-form:send-btn').addEventListener('click', function (event) {
    onUserMessage();
  });
  document.getElementById('ai-chat-form:ai-service-selection').addEventListener('change', function (event) {
    document.getElementById('chat-history').innerHTML = '';
  });
}

function onWebsocketOpen(channel) {
  console.debug('Websocket opened: ' + channel);
}

function onWebsocketClose(code, channel, event) {
  console.debug('Websocket closed: ' + channel);
}

function onWebsocketMessageListener(message, channel, event) {
  newMessage(message, false);
  document.getElementById('ai-chat-form:chat-input-text').disabled = false;
  document.getElementById('ai-chat-form:send-btn').disabled = false;
}

// Non faces impl

let connected = false;

function connect(url) {
  const currentProtocol = window.location.protocol;
  let wsProtocol;
  if (currentProtocol === 'https:') {
    wsProtocol = 'wss';
  } else {
    wsProtocol = 'ws';
  }
  console.log("reconnecting");
  if (connected) {
    socket.close();
  }
  document.getElementById('chat-history').innerHTML = '';
  socket = new WebSocket(wsProtocol + "://" + location.host + url);
  socket.onopen = function () {
    connected = true;
  };
  socket.onmessage = function (m) {
    // console.log("Websocket message: " + m.data);
    newMessage(m.data, false);
    document.getElementById('chat-input-text').disabled = false;
    document.getElementById('send-btn').disabled = false;
    document.getElementById('send-btn').textContent = "Send Message";
  };
}

function sendMessage() {
  if (connected) {
    let message = document.getElementById('chat-input-text').value.trim();
    if (message) {
      // console.log("Sending message: " + message);
      newMessage(message, true);
      socket.send(message);
      document.getElementById('chat-input-text').disabled = true;
      document.getElementById('chat-input-text').value = '';
      document.getElementById('send-btn').disabled = true;
    }
  } else {
    console.error("Sending message failed. Websocket not connected");
  }
}

function initPlain(url) {
  document.getElementById('chat-input-text').addEventListener('keydown', function (event) {
    if (!event.shiftKey && event.key === "Enter") {
      sendMessage();
      event.preventDefault();
    }
  })

  connect(url);
}