//本页面的函数只能在该项目中使用,需要用jq

/*
* 添加的ajax请求,需要传入的值为:
* url :ajax接口地址
* data :传的data值
* obj :添加按钮(点击后不能再点)
* hrefUrl :提交后回到的列表页地址
* */
function ajaxAdd(url,data,obj,hrefUrl) {
    $.ajax({
        type: "POST",
        url: url,
        dataType: 'json',
        data: data,
        success: function (res) {
            if (res.code == 10000) {
                layer.msg(res.msg);
                $(obj).attr("disabled", "disabled")
                setTimeout(function () {
                    window.location.href=hrefUrl
                }, 1500)
            }else{
                layer.msg(res.msg);
            }
        }
    });
}
/*
* 编辑的ajax请求,需要传入的值为:
* url :ajax接口地址
* data :传的data值
* hrefUrl :提交后回到的列表页地址
* */
function ajaxEdit(url,data,hrefUrl) {
    $.ajax({
        type: "POST",
        url: url,
        dataType: 'json',
        data: data,
        success: function (res) {
            if (res.code == 10000) {
                layer.msg(res.msg);
                setTimeout(function () {
                    window.location.href=hrefUrl
                }, 1500)
            }else{
                layer.msg(res.msg);
            }
        }
    });
}

/*
* 删除和恢复操作
* 需要传入的值:
* obj:点击的按钮
* cont:提示的内容
* title:提示的标题
* url:ajax地址
* */
function delRec(obj,cont,title,url) {
    $(obj).click(function(e) {
        e.stopPropagation();
        var id = $(this).attr('data-id')
        layer.confirm(cont, {
            btn: ['确定', '取消'], //按钮
            title: title //按钮
        }, function(index) {
            $.ajax({
                url: url,
                data: {
                    id: id
                },
                dataType: "json",
                method: "POST",
                success: function(res) {
                    if(res.code==10000){
                        layer.msg(res.msg);
                        setTimeout(function() {
                            window.location.reload()
                        }, 1500)
                    }else{
                        layer.msg(res.msg);
                    }
                }
            })
        });
    });
}
//点击放大图片
function bigImg(obj){
    $(obj).on('click',function(){
        var thisrc=$(this).attr('src')
        layer.open({
            type: 1,
            title: false,
            closeBtn: 1,
            shadeClose: true,
            area: ['650px', '650px'], //宽高
            content: "<img src='"+thisrc+"' style='max-width:600px;max-height:600px;display:block;margin:25px auto'/>"
        });
    })
}