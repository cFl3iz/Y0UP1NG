
    <section class="g-flexview">

        <#--<header class="m-navbar">-->
            <#--<a href="../index.html" class="navbar-item"><i class="back-ico"></i></a>-->
            <#--<div class="navbar-center"><span class="navbar-title">CitySelect</span></div>-->
        <#--</header>-->

        <section class="g-scrollview">

            <aside class="demo-tip">
                <p>${uiLabel.CreateOrderSuccess}</p>
            </aside>
            <#if !addressInfo?has_content>
            <div class="m-celltitle">${uiLabel.SetAddress}</div>
            <div class="m-cell">
                <div class="cell-item">
                    <div class="cell-left">${uiLabel.AddressScope}</div>
                    <div class="cell-right cell-arrow">
                        <input type="text" class="cell-input" readonly id="J_Address" placeholder="${uiLabel.SelectAddress}">
                    </div>
                </div>
            </div>
    <div class="m-celltitle">${uiLabel.AddressDetail}</div>
    <div class="m-cell">
        <div class="cell-item">
            <div class="cell-right">
                <textarea class="cell-textarea" placeholder="例:上浦路69弄29号101-501 沈府棋牌"></textarea>
            </div>
        </div>
    </div>
</#if>
        <#if addressInfo?has_content>
            <div class="m-celltitle">${uiLabel.CheckAddress}</div>
            <div class="m-cell">
                <div class="cell-item">
                    <div class="cell-left">${uiLabel.AddressScope}</div>
                    <div class="cell-right cell-arrow">
                        <input type="text" class="cell-input" readonly id="J_Address" placeholder="${uiLabel.SelectAddress}">
                    </div>
                </div>
            </div>
        </#if>

            <#--<div class="m-celltitle">设置默认值</div>-->
            <#--<div class="m-cell">-->
                <#--<div class="cell-item">-->
                    <#--<div class="cell-left">所在地区：</div>-->
                    <#--<div class="cell-right cell-arrow">-->
                        <#--<input type="text" class="cell-input" readonly id="J_Address2" value="新疆 乌鲁木齐市 天山区" placeholder="请选择收货地址">-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->

    <#--<script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>  <script src="../js/ydui.js"></script>-->
    <script src="http://static.ydcss.com/uploads/ydui/ydui.citys.js"></script>

    <script>
        /**
         * 默认调用
         */
        !function () {
            var $target = $('#J_Address');

            $target.citySelect();

            $target.on('click', function (event) {
                event.stopPropagation();
                $target.citySelect('open');
            });

            $target.on('done.ydui.cityselect', function (ret) {
                $(this).val(ret.provance + ' ' + ret.city + ' ' + ret.area);
            });
        }();
        /**
         * 设置默认值
         */
        !function () {
            var $target = $('#J_Address2');

            $target.citySelect({
                provance: '新疆',
                city: '乌鲁木齐市',
                area: '天山区'
            });

            $target.on('click', function (event) {
                event.stopPropagation();
                $target.citySelect('open');
            });

            $target.on('done.ydui.cityselect', function (ret) {
                $(this).val(ret.provance + ' ' + ret.city + ' ' + ret.area);
            });
        }();
    </script>
    <#--<img style="width:80%;height:80%;" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/saomaguanzhu.jpg"/>-->
</section>