<section class="g-flexview">

    <header class="m-navbar">
        <a href="../index.html" class="navbar-item"><i class="back-ico"></i></a>
        <div class="navbar-center"><span class="navbar-title">Cell</span></div>
    </header>

    <section class="g-scrollview">

        <div class="m-cell demo-small-pitch">
            <div class="cell-item">
                <div class="cell-left">Name</div>
                <div class="cell-right"><input type="text" class="cell-input" placeholder="InputYourName" autocomplete="off" /></div>
            </div>
            <div class="cell-item">
                <div class="cell-right cell-arrow"><input type="text" class="cell-input" placeholder="InputYourName" autocomplete="off" /></div>
            </div>
            <div class="cell-item">
                <div class="cell-left">KuaiDi</div>
                <div class="cell-right">ShunFeng</div>
            </div>
        </div>

        <div class="m-celltitle">HasIcon</div>
        <div class="m-cell">
            <div class="cell-item">
                <div class="cell-left"><i class="cell-icon demo-icons-phone"></i></div>
                <div class="cell-right">
                    <input type="number" pattern="[0-9]*" class="cell-input" placeholder="InputYourTele" autocomplete="off" />
                    <a href="javascript:;" class="btn btn-warning">Get Tel Captcha</a>
                </div>
            </div>
            <a class="cell-item" href="javascript:;">
                <div class="cell-left"><i class="cell-icon demo-icons-like"></i>I Love</div>
                <div class="cell-right"><span class="badge badge-danger">8</span></div>
            </a>
            <a class="cell-item" href="tel:400-888-8888">
                <div class="cell-left"><i class="cell-icon demo-icons-tel"></i>Contact Help</div>
                <div class="cell-right cell-arrow">400-888-8888</div>
            </a>
            <a class="cell-item" href="javascript:;">
                <div class="cell-left"><i class="cell-icon demo-icons-order"></i>MyOrder</div>
                <div class="cell-right cell-arrow">ShowAllOrder</div>
            </a>
            <a class="cell-item" href="javascript:;">
                <div class="cell-left"><span class="cell-icon"><img src="http://static.ydcss.com/ydui/img/logo.png" /></span>Icon</div>
                <div class="cell-right cell-arrow">IconIsImage</div>
            </a>
        </div>

        <div class="m-celltitle">Times</div>
        <div class="m-cell">
            <div class="cell-item">
                <div class="cell-left">time1</div>
                <div class="cell-right"><input class="cell-input" type="datetime-local" value="2016-07-19T08:08" placeholder=""/></div>
            </div>
            <div class="cell-item">
                <div class="cell-left">time2</div>
                <div class="cell-right"><input class="cell-input" type="date" value="2016-07-19" placeholder=""/></div>
            </div>
            <div class="cell-item">
                <div class="cell-left">time3</div>
                <div class="cell-right"><input class="cell-input" type="time" value="08:08" placeholder=""/></div>
            </div>
        </div>

        <div class="m-celltitle">SelectOption</div>
        <div class="m-cell">
            <div class="cell-item">
                <label class="cell-right cell-arrow">
                    <select class="cell-select">
                        <option value="">PayMethod</option>
                        <option value="1">ALiPay</option>
                        <option value="2">WeChatPay</option>
                        <option value="3">CaiFuPay</option>
                    </select>
                </label>
            </div>
            <div class="cell-item">
                <div class="cell-left">Gender</div>
                <label class="cell-right cell-arrow">
                    <select class="cell-select">
                        <option value="">ChoesYourSex</option>
                        <option value="1">M</option>
                        <option value="2">FM</option>
                        <option value="3">GAY</option>
                    </select>
                </label>
            </div>
        </div>

        <div class="m-celltitle">MoreSelect</div>
        <div class="m-cell">
            <label class="cell-item">
                <span class="cell-left">2Choes1</span>
                <label class="cell-right">
                    <input type="checkbox" value="1" name="checkbox"/>
                    <i class="cell-checkbox-icon"></i>
                </label>
            </label>
            <label class="cell-item">
                <span class="cell-left">MoreChoesTwo</span>
                <label class="cell-right">
                    <input type="checkbox" value="2" name="checkbox"/>
                    <i class="cell-checkbox-icon"></i>
                </label>
            </label>
            <label class="cell-item">
                <span class="cell-left">MoreChoesThree</span>
                <label class="cell-right">
                    <input type="checkbox" value="3" name="checkbox"/>
                    <i class="cell-checkbox-icon"></i>
                </label>
            </label>
        </div>

        <div class="m-celltitle">Redio</div>
        <div class="m-cell">
            <label class="cell-item">
                <span class="cell-left">SelectOnlyOne</span>
                <label class="cell-right">
                    <input type="radio" value="man" name="radio" checked/>
                    <i class="cell-radio-icon"></i>
                </label>
            </label>
            <label class="cell-item">
                <span class="cell-left">SelectTwo</span>
                <label class="cell-right">
                    <input type="radio" value="woman" name="radio"/>
                    <i class="cell-radio-icon"></i>
                </label>
            </label>
        </div>

        <div class="m-celltitle">DoubleChose</div>
        <div class="m-cell">
            <label class="cell-item">
                <span class="cell-left">Do Default Location</span>
                <label class="cell-right">
                    <input type="checkbox" class="m-switch"/>
                </label>
            </label>
            <label class="cell-item">
                <span class="cell-left">FireFox IS oK</span>
                <label class="cell-right">
                    <input type="checkbox" class="m-switch-old" checked/>
                    <span class="m-switch"></span>
                </label>
            </label>
        </div>

        <div class="m-celltitle">Input TextArea</div>
        <div class="m-cell">
            <div class="cell-item">
                <div class="cell-right">
                    <textarea class="cell-textarea" placeholder="PleaseInputYourBankPwd"></textarea>
                </div>
            </div>
        </div>

    </section>
