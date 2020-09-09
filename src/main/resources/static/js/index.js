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
function create(){
    var releaseTime = $("input[name='releaseTime']").val();
    var days = $("input[name='days']").val();
    var reg=/^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/;
    if(releaseTime==''||releaseTime==null||days==''||days==null){
        alert("提交数据不能为空");
        return false;
    }
    if(!reg.test(releaseTime)){
        alert("文章发表日期输入有误！");
        return false;
    }
    if(isNaN(days)){
        alert("只能输入数字！");
        return false;
    }
    if (!/(^[1-9]\d*$)/.test(days)){
        alert("请输入正整数！");
        return false;
    }
    if(days<0||days>999){
        alert("天数最多设置999天，最少设置1天");
        return false;
    }
}

function create2(){
    var maxUser = $("input[name='maxUser']").val();
    var integral = $("input[name='integral']").val();
    var days2 = $("input[name='days2']").val();
    if(maxUser==''||maxUser==null||integral==''||integral==null){
        alert("提交数据不能为空");
        return false;
    }
    if(isNaN(maxUser)||isNaN(integral)){
        alert("只能输入数字！");
        return false;
    }
    if (!/(^[1-9]\d*$)/.test(maxUser)||!/(^[1-9]\d*$)/.test(integral)){
        alert("请输入正整数！");
        return false;
    }
    if(days2<0||days2>999){
        alert("天数最多设置999天，最少设置1天");
        return false;
    }
    if(maxUser<0||maxUser>99999){
        alert("人数最大设置为99999天，最小设置1");
        return false;
    }
}

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
    var size = $("input[name='inputGroupFile02']")[0].files[0].size/1024;
    if(size>10240){
        alert("上传文件格式大于10MB\n上传失败");
        return false;
    }
}

