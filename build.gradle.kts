
import myaa.subkt.tasks.*
import myaa.subkt.tasks.Mux.*
import myaa.subkt.tasks.Nyaa.*
import myaa.subkt.ass.EventLineAccessor
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
                syncTo(getAs<Duration>("opsync"))
            }
        }

        if (propertyExists("ED")) {
            from(get("ED")) {
                syncTo(getAs<Duration>("edsync"))
            }
        }

        if (file(get("IS")).exists()) {
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

        out(get("muxout"))
    }

    torrent {
        from(mux.item())
        trackers(getList("trackers"))
        out(get("single_ep"))
    }
}
