var i = 0;
var txt = 'impressive';
var speed = 50;

function typeWriter() {
  if (i < txt.length) {
    document.getElementById("typingEffect").innerHTML += txt.charAt(i);
    i++;
    setTimeout(typeWriter, speed);
  }
}

typeWriter();
