$(function () {
    $("#inputGroupFile02").change(function () {
        var filepath = $("input[name='inputGroupFile02']").val();
        $("#label_1").text(filepath);
    });

    $(document).on("click","#but_2",function () {
        if(confirm("确定要删除吗？")){
            var mediaId=$(this).val();
            $.ajax({
                url:'/delmaterial?mediaId='+mediaId,
                dataType:'JSON',
                type:'GET',
                success:function(msg) {
                    console.log(msg);
                    if (msg == 0) {
                        window.location.reload();
                    } else if(msg==1){
                        alert("删除失败!");
                    }
                }
            })
        }else {
            return  false;
        }
    });
});

function check(){
    var filepath = $("input[name='inputGroupFile02']").val();
    if(filepath==null||filepath==''){
        alert("上传海报不能为null");
        return false;
    }else {
        var extStart = filepath.lastIndexOf('.');
        var   ext = filepath.substring(extStart, filepath.length).toUpperCase();
        console.log(ext);
        if (ext !== '.PNG' && ext !== '.JPG' && ext !== '.JPEG') {
            alert("上传文件格式不正确");
            return false;
        }
    }
}