$(function () {
   var text=$(".hh").text();
   if(text.length>1){
       text=text.charAt(text.length-1)
   }
   if (isNaN(text)){
       $("#img_img").hide()
   }
});