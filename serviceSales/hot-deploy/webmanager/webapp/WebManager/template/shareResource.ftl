
<style>
    a{
        text-decoration:none;
    }
</style>
<section class="g-flexview">


    <article class="m-list list-theme4">
        <a href="javascript:;" class="list-item">
            <div class="list-img">
                <img src="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1578778163,3745513912&fm=27&gp=0.jpg" data-url="http://img1.shikee.com/try/2016/06/19/09430120929215230041.jpg_180x180.jpg">
            </div>
            <div class="list-mes">
                <h2 class="list-title">My Pets</h2>
                <div class="list-mes-item">
                    <div>
                        <span class="list-price"><em>$</em>34.7</span>
                        <span class="list-del-price">$45.65</span>
                    </div>
                    <div>Canada</div>
                </div>
            </div>
        </a>

    </article>

    <article class="m-list list-theme4">
         <strong>
             I'm leaving Canada, so all pets are sold at a low price!
         </strong>
    </article>


    <div class="m-slider" id="J_Slider">
        <div class="slider-wrapper">
            <div class="slider-item">
                <a href="#">
                    <img src="http://static.ydcss.com/uploads/ydui/1.jpg">
                </a>
            </div>
            <div class="slider-item">
                <a href="#">
                    <img src="http://static.ydcss.com/uploads/ydui/2.jpg">
                </a>
            </div>
            <div class="slider-item">
                <a href="#">
                    <img src="http://static.ydcss.com/uploads/ydui/3.jpg">
                </a>
            </div>
        </div>
        <div class="slider-pagination"></div><!-- 分页标识 -->
    </div>
    <script>
        $('#J_Slider').slider({
            speed: 200,
            autoplay: 2000,
            lazyLoad: true
        });
    </script>
    <br/>
    <footer class="m-tabbar demo-small-pitch" style="background-color:#ff5647;bottom: 0;">

        <a href="../index.html" class="tabbar-item">
                <#--<span class="tabbar-icon">-->
                    <#--<i class="demo-icons-contact"></i>-->
                <#--</span>-->
            <span class="tabbar-txt" style="color:#ffffff;font-size:22px;">${uiLabel.Talk}</span>
        </a>

    </footer>
</section>



