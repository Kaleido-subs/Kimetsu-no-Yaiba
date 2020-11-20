import myaa.subkt.ass.*
import myaa.subkt.tasks.*
import myaa.subkt.tasks.Mux.*
import myaa.subkt.tasks.Nyaa.*
import java.awt.Color
import java.time.*

plugins {
    id("myaa.subkt")
}

subs {
    readProperties("sub.properties")
    episodes(getList("episodes"))

    merge {
        from(get("dialogue"))

        if (propertyExists("OP")) {
            from(get("OP")) {
                syncTargetTime(getAs<Duration>("opsync"))
            }
        }

        if (propertyExists("ED")) {
            from(get("ED")) {
                syncTargetTime(getAs<Duration>("edsync"))
            }
        }

        if (propertyExists("IS")) {
            from(get("IS"))
        }

        if (propertyExists("TS")) {
            from(get("TS"))
        }

        out(get("mergedname"))
    }

    chapters {
        from(merge.item())
        chapterMarker("chapter")
    }


    mux {
        title(get("title"))

        from(get("premux")) {
            includeChapters(false)
			attachments { include(false) }
        }

		from(merge.item()) {
			tracks {
				lang("eng")
                name(get("group"))
				default(true)
				forced(false)
				compression(CompressionType.ZLIB)
			}
		}

        chapters(chapters.item()) { lang("eng") }

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
}
