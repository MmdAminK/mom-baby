package com.app.mombaby.data

import com.app.mombaby.models.adapterModels.Option
import com.app.mombaby.models.adapterModels.Question

object DepressionTestData {

    val questions = arrayListOf(
        Question(
            questionTitle = "غمگینی", optionList = arrayListOf(
                Option(optionTitle = "احساس غمگینی نمی کنم", score = 0f),
                Option(optionTitle = "خیلی اوقات احساس غمگینی می کنم", score = 1f),
                Option(optionTitle = "همیشه غمگین هستم", score = 2f),
                Option(optionTitle = "به قدری غمگین هستم که نمی توانم تحمل کنم", score = 3f)
            )
        ),
        Question(
            questionTitle = "بدبینی", optionList = arrayListOf(
                Option(optionTitle = "نسبت به آینده بدبین نیستم", score = 0f),
                Option(optionTitle = "بیشتر از گذشته، نسبت به آینده بدبین هستم", score = 1f),
                Option(optionTitle = "انتظار ندارم اوضاع بر وفق مراد من باشد", score = 2f),
                Option(
                    optionTitle = "احساس می کنم امیدی به آینده نیست و اوضاع فقط بدتر می شود",
                    score = 3f
                )
            )
        ),
        Question(
            questionTitle = "احساس شکست", optionList = arrayListOf(
                Option(optionTitle = "احساس نمی کنم فردی شکست خورده ام", score = 0f),
                Option(optionTitle = "بیش از آنچه که سزاوار بودم، شکست خورده ام", score = 1f),
                Option(
                    optionTitle = "وقتی به آینده می نگرم، شکست های زیادی را می بینم",
                    score = 2f
                ),
                Option(optionTitle = "احساس میکنم شخص کاملا شکست خورده ای هستم", score = 3f)
            )
        ),
        Question(
            questionTitle = "نارضایتی", optionList = arrayListOf(
                Option(optionTitle = "به اندازه گذشته، از زندگی لذت می برم", score = 0f),
                Option(optionTitle = "دیگر به اندازه گذشته از زندگی لذت نمی برم", score = 1f),
                Option(
                    optionTitle = "از چیزهایی که در گذشته از آنها لذت می بردم، خیلی کم لذت می برم",
                    score = 2f
                ),
                Option(
                    optionTitle = "اصلا نمی توانم از چیز هایی که قبلا از آن ها لذت می بردم،هیچ لذتی ببرم",
                    score = 3f
                )
            )
        ),
        Question(
            questionTitle = "احساس گناه", optionList = arrayListOf(
                Option(optionTitle = "احساس گناه خاصی ندارم", score = 0f),
                Option(
                    optionTitle = "در مورد خیلی از چیز هایی که انجام داده ام و یا باید انجام می دادم احساس گناه میکنم",
                    score = 1f
                ),
                Option(optionTitle = "اغلب اوقات احساس گناه می کنم", score = 2f),
                Option(optionTitle = "همواره احساس گناه می کنم", score = 3f)
            )
        ),
        Question(
            questionTitle = "انتظار تنبیه", optionList = arrayListOf(
                Option(optionTitle = "احساس نمی کنم دارم تنبیه می شوم", score = 0f),
                Option(optionTitle = "احساس می کنم ممکن است تنبیه شوم", score = 1f),
                Option(optionTitle = "من انتظار تنبیه شدن را دارم", score = 2f),
                Option(optionTitle = "احساس می کنم که دارم تنبیه می شوم", score = 3f)
            )
        ),
        Question(
            questionTitle = "دوست نداشتن خود", optionList = arrayListOf(
                Option(
                    optionTitle = "همان احساسی را در مورد خودم دارم که همیشه داشته ام",
                    score = 0f
                ),
                Option(optionTitle = "اعتماد به نفسم را از دست داده ام", score = 1f),
                Option(optionTitle = "از خودمم مأیوس شده ام", score = 2f),
                Option(optionTitle = "از خودم بدم می آید", score = 3f)
            )
        ),
        Question(
            questionTitle = "خود سرزنشی", optionList = arrayListOf(
                Option(
                    optionTitle = "بیشتر از حد معمول، خود را مورد انتقاد وسرزنش قرار نمی دهم",
                    score = 0f
                ),
                Option(
                    optionTitle = "بیشتر از گذشته، از خودم انتقاد می کنم",
                    score = 1f
                ),
                Option(
                    optionTitle = "بخاطر تمامی اشتباهاتم، از خودم انتقاد می کنم",
                    score = 2f
                ),
                Option(
                    optionTitle = "برای هر چیز بدی که اتفاق می افتد خود را سرزنش می کنم",
                    score = 3f
                )
            )
        ),
        Question(
            questionTitle = "افکار خودکشی", optionList = arrayListOf(
                Option(optionTitle = "اصلا در فکر آن نیستم که به خودم آسیب برسانم", score = 0f),
                Option(
                    optionTitle = "درباره اینکه به خودم آسیبی برسانم فکر می کنم، ولی این کار را نمی کنم",
                    score = 1f
                ),
                Option(optionTitle = "دلم می خواهد خودم را بکشم", score = 2f),
                Option(optionTitle = "اگر امکان داشت خودم را میکشتم", score = 3f)
            )
        ),
        Question(
            questionTitle = "گریه کردن", optionList = arrayListOf(
                Option(optionTitle = "بیشتر از گذشته، گریه نمی کنم", score = 0f),
                Option(optionTitle = "بیشتر از گذشته، گریه می کنم", score = 1f),
                Option(optionTitle = "بخاطر هر چیز کوچکی، گریه می کنم", score = 2f),
                Option(optionTitle = "دلم می خواهد گریه کنم اما نمی توانم", score = 3f)
            )
        ),
        Question(
            questionTitle = "بی قراری", optionList = arrayListOf(
                Option(optionTitle = "بیش از حد معمول بی قرار و تحریک پذیر نیستم", score = 0f),
                Option(
                    optionTitle = "احساس می کنم بیشتر از حد معمول، بی قرار و تحریک پذیر شده ام",
                    score = 1f
                ),
                Option(
                    optionTitle = "به قدری بی قرار و ناراحت هستم که نمی توانم آرام بگیرم",
                    score = 2f
                ),
                Option(
                    optionTitle = "به قدری بی قرار و ناراحت هستم که باید دایما یا حرکت کنم یا به کاری مشغول باشم",
                    score = 3f
                )
            )
        ),
        Question(
            questionTitle = "کناره گیری اجتماعی", optionList = arrayListOf(
                Option(
                    optionTitle = "علاقه ام را نسبت به مردم و فعالیت ها از دست نداده ام",
                    score = 0f
                ),
                Option(
                    optionTitle = "در مقایسه با قبل، کمتر به مردم و چیز ها علاقه دارم",
                    score = 1f
                ),
                Option(
                    optionTitle = "بیشتر علاقه ام را نسبت به مردم و چیز ها از دست داده ام",
                    score = 2f
                ),
                Option(optionTitle = "علاقه مند شدن به هر چیز برایم دشوار است", score = 3f)
            )
        ),
        Question(
            questionTitle = "بی تصمیمی", optionList = arrayListOf(
                Option(optionTitle = "تقریبا به خوبی گذشته، تصمیم گیری می کنم", score = 0f),
                Option(optionTitle = "تصمیم گیری برایم دشوارتر از حد معمول است", score = 1f),
                Option(optionTitle = "بیشتر از گذشته، در تصمیم گیری مشکل دارم", score = 2f),
                Option(optionTitle = "در گرفتن هر نوع تصمیمی، مشکل دارم", score = 3f)
            )
        ),
        Question(
            questionTitle = "بی ارزشی", optionList = arrayListOf(
                Option(optionTitle = "احساس میکنم آدم با ارزشی هستم", score = 0f),
                Option(
                    optionTitle = "احساس نمی کنم به اندازه گذشته، ارزشمند و مفید باشم",
                    score = 1f
                ),
                Option(optionTitle = "در مقایسه با دیگران، خود را کم ارزش تر می دانم", score = 2f),
                Option(optionTitle = "بی نهایت احساس بی ارزشی می کنم", score = 3f)
            )
        ),
        Question(
            questionTitle = "از دست دادن انرژی", optionList = arrayListOf(
                Option(optionTitle = "من به اندازه گذشته، انرژی دارم", score = 0f),
                Option(optionTitle = "نسبت به گذشته، انرژی ام کمتر شده است", score = 1f),
                Option(optionTitle = "انرژی لازم برای انجام کارهای زیاد را ندارم", score = 2f),
                Option(optionTitle = "انرژی انجام هیچ کاری را ندارم", score = 3f)
            )
        ),
        Question(
            questionTitle = "تغییر در الگوی خواب", optionList = arrayListOf(
                Option(optionTitle = "در الگوی خوابم، هیچ تغییری ایجاد نشده است", score = 0f),
                Option(optionTitle = "کمی بیش از حد معمول می خوابم", score = 0.5f),
                Option(optionTitle = "تا حدودی کمتر از حد معمول می خوابم", score = 1f),
                Option(optionTitle = "خیلی بیش از حد معمول می خوابم", score = 1.5f),
                Option(optionTitle = "خیلی کمتر از حد معمول می خوابم", score = 2f),
                Option(optionTitle = "بیشتر اوقات روز را می خوابم", score = 2.5f),
                Option(
                    optionTitle = "صبح ها یک تا دو ساعت زودتر از خواب بیدار می شوم و دیگر نمی توانم بخوابم",
                    score = 3f
                )
            )
        ),
        Question(
            questionTitle = "تحریک پذیری", optionList = arrayListOf(
                Option(optionTitle = "بیش از حد معمول تحریک پذیر نیستم", score = 0f),
                Option(optionTitle = "بیش از حد معمول تحریک پذیر هستم", score = 1f),
                Option(optionTitle = "خیلی بیش از حد معمول تحریک پذیر هستم", score = 2f),
                Option(optionTitle = "همیشه تحریک پذیر هستم", score = 3f)
            )
        ),
        Question(
            questionTitle = "تغییر در اشتها", optionList = arrayListOf(
                Option(optionTitle = "اشتهایم تغییری نکرده است", score = 0f),
                Option(optionTitle = "اشتهایم کمتر از حد معمول است", score = 0.5f),
                Option(optionTitle = "اشتهایم بیشتر از حد معمول است", score = 1f),
                Option(optionTitle = "اشتهایم خیلی کمتر از حد معمول است", score = 1.5f),
                Option(optionTitle = "اشتهایم خیلی بیشتر از حد معمول است", score = 2f),
                Option(optionTitle = "اصلا اشتها ندارم", score = 2.5f),
                Option(optionTitle = "همیشه میل زیادی به غذا خوردن دارم", score = 3f)
            )
        ),
        Question(
            questionTitle = "اشکال در تمرکز", optionList = arrayListOf(
                Option(optionTitle = "تمرکزم به خوبی گذشته است", score = 0f),
                Option(optionTitle = "نمی توانم به خوبی گذشته، تمرکز داشته باشم", score = 1f),
                Option(
                    optionTitle = "نمی توانم فکرم را روی موضوعی به مدت طولانی متمرکز کنم",
                    score = 2f
                ),
                Option(optionTitle = "احساس میکنم نمی توانم روی هیچ چیزی تمرکز کنم", score = 3f)
            )
        ),
        Question(
            questionTitle = "خستگی پذیری", optionList = arrayListOf(
                Option(optionTitle = "بیش از حد معمول، خسته یا کسل نیستم", score = 0f),
                Option(optionTitle = "زودتر از حد معمول، خسته یا کسل می شوم", score = 1f),
                Option(
                    optionTitle = "به قدری خسته یا کسل هستم که نمی توانم کارهایی را که قبلا انجام می دادم،انجام دهم",
                    score = 2f
                ),
                Option(
                    optionTitle = "به قدری خسته یا کسل هستم که نمی توانم اغلب کارهایی را که قبلا انجام می دادم، انجام دهم",
                    score = 3f
                )
            )
        ),
        Question(
            questionTitle = "کاهش علاقه جنسی", optionList = arrayListOf(
                Option(optionTitle = "متوجه تغییر تازه ای در علاقه جنسی ام نشده ام", score = 0f),
                Option(optionTitle = "کمتر از گذشته به امور جنسی علاقه مندم", score = 1f),
                Option(optionTitle = "در حال حاضر خیلی کم به امور جنسی علاقه دارم", score = 2f),
                Option(optionTitle = "علاقه جنسی ام را کاملا از دست داده ام", score = 3f)
            )
        )
    )
}