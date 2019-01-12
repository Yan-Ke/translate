;(function(undefined) {
    var _global;
    //工具函数
    //配置合并
    function extend(def, opt, override) {
        for (var k in opt) {
            if (opt.hasOwnProperty(k) && (!def.hasOwnProperty(k) || override)) {
                def[k] = opt[k]
            }
        }
        return def;
    }
    //日期格式化
    function formartDate(y, m, d, symbol) {
        symbol = symbol || '-';
        m = (m.toString())[1] ? m : '0' + m;
        d = (d.toString())[1] ? d : '0' + d;
        return y + symbol + m + symbol + d
    }

    function Schedule(opt) {
        var def = {},
            opt = extend(def, opt, true),
            curDate = opt.date ? new Date(opt.date) : new Date(),
            year = curDate.getFullYear(),
            month = curDate.getMonth(),
            day = curDate.getDate(),
            currentYear = curDate.getFullYear(),
            currentMonth = curDate.getMonth(),
            currentDay = curDate.getDate(),
            selectedDate = '',
            el = document.querySelector(opt.el) || document.querySelector('body'),
            _this = this;
        var bindEvent = function() {
            el.addEventListener('click', function(e) {
                switch (e.target.id) {
                    case 'nextMonth':
                        _this.nextMonthFun();
                        break;
                    case 'nextYear':
                        _this.nextYearFun();
                        break;
                    case 'prevMonth':
                        _this.prevMonthFun();
                        break;
                    case 'prevYear':
                        _this.prevYearFun();
                        break;
                    default:
                        break;
                };
                if (e.target.className.indexOf('currentDate') > -1) {
                    opt.clickCb && opt.clickCb(year, month + 1, e.target.innerHTML);
                    selectedDate = e.target.title;
                    day = e.target.innerHTML;

                }
            }, false)
        }
        var init = function() {
            var scheduleHd = '<div class="schedule-hd">' +
                '<div>' +
                '<span class="arrow icon iconfont icon-jiantou" id="prevMonth"></span>' +
                '<span class="arrow icon iconfont icon-jiantou2" id="prevYear" ></span>' +
                '</div>' +
                '<div class="today">' + formartDate(year, month + 1, day, '-') + '</div>' +
                '<div>' +
                '<span class="arrow icon iconfont icon-jiantou1" id="nextYear"></span>' +
                '<span class="arrow icon iconfont icon-jiantou3" id="nextMonth"></span>' +
                '</div>' +
                '</div>'
            var scheduleWeek = '<ul class="week-ul ul-box">' +
                '<li>日</li>' +
                '<li>一</li>' +
                '<li>二</li>' +
                '<li>三</li>' +
                '<li>四</li>' +
                '<li>五</li>' +
                '<li>六</li>' +
                '</ul>'
            var scheduleBd = '<ul class="schedule-bd ul-box" ></ul>';
            el.innerHTML = scheduleHd + scheduleWeek + scheduleBd;
            bindEvent();
            render();
        }
        var render = function() {
            var fullDay = new Date(year, month + 1, 0).getDate(), //当月总天数
                startWeek = new Date(year, month, 1).getDay(), //当月第一天是周几
                total = (fullDay + startWeek) % 7 == 0 ? (fullDay + startWeek) : fullDay + startWeek + (7 - (fullDay + startWeek) % 7), //元素总个数
                lastMonthDay = new Date(year, month, 0).getDate(), //上月最后一天
                eleTemp = [];

            for (var i = 0; i < total; i++) {
                if (i < startWeek) {
                    eleTemp.push('<li class="other-month"><span class="dayStyle">' + (lastMonthDay - startWeek + 1 + i) + '</span></li>')
                } else if (i < (startWeek + fullDay)) {
                    var nowDate = formartDate(year, month + 1, (i + 1 - startWeek), '-');
                    var addClass = '';
                    selectedDate == nowDate && (addClass = 'selected-style');
                    //	当天的日期显示/隐藏				formartDate(currentYear,currentMonth+1,currentDay,'-') == nowDate && (addClass = 'today-flag');
                    eleTemp.push('<li class="current-month" ><span title=' + nowDate + ' class="currentDate dayStyle ' + addClass + '">' + (i + 1 - startWeek) + '</span><i class="iconfont icon-xuanzhongtishix_fuzhi_fuzhi-" style="display:none"></i></li>')
                } else {
                    eleTemp.push('<li class="other-month"><span class="dayStyle">' + (i + 1 - (startWeek + fullDay)) + '</span></li>')
                }
            }
            el.querySelector('.schedule-bd').innerHTML = eleTemp.join('');
            el.querySelector('.today').innerHTML = formartDate(year, month + 1, day, '-');
            //下面是点击选择的方法
            var aDa = el.querySelector('.schedule-bd').getElementsByClassName('currentDate')
            var sDa = el.querySelector('.schedule-bd').getElementsByClassName('selected-style')
            var at = document.getElementsByClassName('today')[0].innerHTML.split('-');
            document.getElementsByClassName('today')[0].innerHTML = at[0] + '年' + at[1] + '月'
            var arr = []
            var aDd = el.querySelector('.schedule-bd').getElementsByClassName('current-month')
            document.getElementById('dateCancel').onclick = function() {
                document.getElementById('prevYear').style.display = "block";
                document.getElementById('prevMonth').style.display = "block";
                document.getElementById('nextMonth').style.display = "block";
                document.getElementById('nextYear').style.display = "block";
                document.getElementById('twoBtn').style.display = "none";
                document.getElementById('oneBtn').style.display = "block";
                // window.location.reload()
                render()
            }
            document.getElementById('dateBtn').onclick = function() {
                document.getElementById('oneBtn').style.display = "none";
                document.getElementById('prevYear').style.display = "none";
                document.getElementById('prevMonth').style.display = "none";
                document.getElementById('nextMonth').style.display = "none";
                document.getElementById('nextYear').style.display = "none";
                document.getElementById('twoBtn').style.display = "block";

                for (let i = 0; i < aDa.length; i++) {
                    aDa[i].onclick = function() {
                        if (this.className == "currentDate dayStyle selected-style") {
                            this.className = "currentDate dayStyle"
                            aDd[i].getElementsByClassName('iconfont')[0].style.display = 'none'
                        } else {
                            this.className = "currentDate dayStyle selected-style"
                            aDd[i].getElementsByClassName('iconfont')[0].style.display = 'block'
                            aDd[i].getElementsByClassName('iconfont')[0].style.right = "8px"
                            aDd[i].getElementsByClassName('iconfont')[0].style.top = '-16px'
                        }
                        console.log(this.innerHTML)
                    }
                }
            }
            document.getElementById('dateEdit').onclick = function() {
                var sDaArr = []
                for (let i = 0; i < sDa.length; i++) {
                    sDaArr.push(sDa[i].innerHTML)
                }
                console.log(sDaArr, at[0], at[1])
                $.ajax({
                    url: '/san/operation/calendar_add',
                    data: {
                        year: at[0],
                        month: at[1],
                        date: sDaArr
                    },
                    dataType: 'json',
                    type: 'POST',
                    success: function(res) {
                        console.log(res)
                        if (res.code == 10000) {
                            layer.msg('保存成功')
                            setTimeout(function() {
                                render()
                            }, 1500)
                        } else {
                            layer.msg('保存失败')
                        }
                    }
                })
            }

            $.ajax({
                url: "/san/operation/calendar",
                data: {
                    year: at[0],
                    month: at[1]
                },
                dataType: "json",
                type: "POST",
                success: function(res) {
                    document.getElementById('prevYear').style.display = "block";
                    document.getElementById('prevMonth').style.display = "block";
                    document.getElementById('nextMonth').style.display = "block";
                    document.getElementById('nextYear').style.display = "block";
                    document.getElementById('oneBtn').style.display = "block";
                    document.getElementById('twoBtn').style.display = "none";
                    console.log(res)
                    arr = res.days
                    document.getElementById('yearDays').innerHTML = res.yearDays
                    document.getElementById('monthDays').innerHTML = res.day
                    document.getElementById('yyYear1').innerHTML = at[0]
                    document.getElementById('yyYear2').innerHTML = at[0]
                    document.getElementById('yyMonth').innerHTML = at[1]
                    for (let i = 0; i < aDa.length; i++) {
                        aDd[i].getElementsByClassName('iconfont')[0].style.position = 'absolute'
                        aDd[i].getElementsByClassName('iconfont')[0].style.height = '5px'
                        aDd[i].getElementsByClassName('iconfont')[0].style.color = '#44DB5E'
                        aDa[i].className = "currentDate dayStyle"
                        var _index = i
                        for (let j = 0; j < arr.length; j++) {
                            if (aDa[_index].innerHTML == arr[j]) {
                                aDa[_index].className = "currentDate dayStyle selected-style"
                                aDd[_index].getElementsByClassName('iconfont')[0].style.display = 'block'
                                aDd[_index].getElementsByClassName('iconfont')[0].style.right = "8px"
                                aDd[_index].getElementsByClassName('iconfont')[0].style.top = '-16px'
                            }
                        }
                    }
                }

            })


        };
        this.nextMonthFun = function() {
                if (month + 1 > 11) {
                    year += 1;
                    month = 0;
                } else {
                    month += 1;
                }
                render();
                opt.nextMonthCb && opt.nextMonthCb(year, month + 1, day);
            },
            this.nextYearFun = function() {
                year += 1;
                render();
                opt.nextYeayCb && opt.nextYeayCb(year, month + 1, day);
            },
            this.prevMonthFun = function() {
                if (month - 1 < 0) {
                    year -= 1;
                    month = 11;
                } else {
                    month -= 1;
                }
                render();
                opt.prevMonthCb && opt.prevMonthCb(year, month + 1, day);
            },
            this.prevYearFun = function() {
                year -= 1;
                render();
                opt.prevYearCb && opt.prevYearCb(year, month + 1, day);
            }
        init();
    }
    //将插件暴露给全局对象
    _global = (function() { return this || (0, eval)('this') }());
    if (typeof module !== 'undefined' && module.exports) {
        module.exports = Schedule;
    } else if (typeof define === "function" && define.amd) {
        define(function() {
            return Schedule;
        })
    } else {
        !('Schedule' in _global) && (_global.Schedule = Schedule);
    }

}());