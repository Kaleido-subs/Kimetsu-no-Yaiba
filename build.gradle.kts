import myaa.subkt.ass.*
import myaa.subkt.tasks.*
import myaa.subkt.tasks.Mux.*
import myaa.subkt.tasks.Nyaa.*
import java.awt.Color
import java.time.*

plugins {
    id("myaa.subkt")
}

fun String.isKaraTemplate(): Boolean {
    return this.startsWith("code") || this.startsWith("template") || this.startsWith("mixin")
}

fun EventLine.isKaraTemplate(): Boolean {
    return this.comment && this.effect.isKaraTemplate()
}

subs {
    readProperties("sub.properties")
    episodes(getList("episodes"))

    val op_ktemplate by task<Automation> {
        if (propertyExists("OP")) {
            from(get("OP"))
        }

        video(get("premux"))
        script("0x.KaraTemplater.moon")
        macro("0x539's Templater")
        loglevel(Automation.LogLevel.WARNING)
    }

    val ed_ktemplate by task<Automation> {
        if (propertyExists("ED")) {
            from(get("ED"))
        }

        video(get("premux"))
        script("0x.KaraTemplater.moon")
        macro("0x539's Templater")
        loglevel(Automation.LogLevel.WARNING)
    }

    merge {
        from(get("dialogue"))

        if (propertyExists("OP")) {
            from(op_ktemplate.item()) {
                syncTargetTime(getAs<Duration>("opsync"))
            }
        }

        if (propertyExists("ED")) {
            from(ed_ktemplate.item()) {
                syncTargetTime(getAs<Duration>("edsync"))
            }
        }

        if (file(get("IS")).exists()) {
            from(get("IS"))
        }

        if (file(get("TS")).exists()) {
            from(get("TS"))
        }

        out(get("mergedname"))
    }

    val cleanmerge by task<ASS> {
        from(merge.item())
        ass {
            events.lines.removeIf { it.isKaraTemplate() }
        }
    }

    chapters {
        from(merge.item())
        chapterMarker("chapter")
    }


    mux {
        title(get("title"))

        from(get("premux")) {
            video {
                lang("jpn")
                default(true)
            }
            audio {
                lang("jpn")
                default(true)
            }
            includeChapters(false)
            attachments { include(false) }
        }

        from(cleanmerge.item()) {
            tracks {
                lang("eng")
                name(get("group"))
                default(true)
                forced(true)
                compression(CompressionType.ZLIB)
            }
        }

        chapters(chapters.item()) { lang("eng") }

        attach(get("common_fonts")) {
            includeExtensions("ttf", "otf")
        }

        attach(get("fonts")) {
            includeExtensions("ttf", "otf")
        }

        if (propertyExists("OP")) {
            attach(get("opfonts")) {
                includeExtensions("ttf", "otf")
            }
        }

        if (propertyExists("ED")) {
            attach(get("edfonts")) {
                includeExtensions("ttf", "otf")
            }
        }

        out(get("muxout"))
    }


    tasks(getList("ncs")) {
        // will create mux.ncop1, mux.ncop2, etc tasks
        val run_ktemplate by task<Automation> {
            from(get("nc_subs"))

            video(get("ncpremux"))
            script("0x.KaraTemplater.moon")
            macro("0x539's Templater")
            loglevel(Automation.LogLevel.WARNING)
        }

        merge {
            from(run_ktemplate.item())

            includeExtraData(false)
            includeProjectGarbage(false)

            scriptInfo {
                title = "Kaleido-Flax"
                scaledBorderAndShadow = true
            }
        }

        val cleanmerge by task<ASS> {
            from(merge.item())
            ass {
                events.lines.removeIf { it.isKaraTemplate() }
            }
        }

        chapters {
            from(cleanmerge.item())
            chapterMarker("chapter")
        }

        mux {
            title(get("title"))

            from(get("ncpremux")) {
                video {
                    lang("jpn")
                    default(true)
                }
                audio {
                    lang("jpn")
                    default(true)
                }
                includeChapters(false)
                attachments { include(false) }
            }

            from(cleanmerge.item()) {
                tracks {
                    lang("eng")
                    name(get("group"))
                    default(true)
                    forced(true)
                    compression(CompressionType.ZLIB)
                }
            }

            chapters(chapters.item()) { lang("eng") }

            attach(get("ncfonts"))

            out(get("muxout"))
        }
    }
}
