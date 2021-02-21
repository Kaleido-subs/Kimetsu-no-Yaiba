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

    // Muxing NCs (really wish I knew how to do it neater)
    // NCOP1v1
	tasks(listOf("NCOP1")) {
        val ncop1_ktemplate by task<Automation> {
            from(get("ncop_ass"))

            video(get("ncop1_premux"))
            script("0x.KaraTemplater.moon")
            macro("0x539's Templater")
            loglevel(Automation.LogLevel.WARNING)
        }

		merge {
			from(ncop1_ktemplate.item())

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
			title(get("ncop1_muxtitle"))

			from(get("ncop1_premux")) {
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

			attach(get("opfonts"))

			out(get("ncop1_muxout"))
		}
	}

    // NCOP1v2
	tasks(listOf("NCOP2")) {
        val ncop2_ktemplate by task<Automation> {
            from(get("ncop_ass"))

            video(get("ncop2_premux"))
            script("0x.KaraTemplater.moon")
            macro("0x539's Templater")
            loglevel(Automation.LogLevel.WARNING)
        }

		merge {
			from(ncop2_ktemplate.item())

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
			title(get("ncop2_muxtitle"))

			from(get("ncop2_premux")) {
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

			attach(get("opfonts"))

			out(get("ncop2_muxout"))
		}
	}

    // NCOP1v3
	tasks(listOf("NCOP3")) {
        val ncop3_ktemplate by task<Automation> {
            from(get("ncop_ass"))

            video(get("ncop3_premux"))
            script("0x.KaraTemplater.moon")
            macro("0x539's Templater")
            loglevel(Automation.LogLevel.WARNING)
        }

		merge {
			from(ncop3_ktemplate.item())

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
			title(get("ncop3_muxtitle"))

			from(get("ncop3_premux")) {
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

			attach(get("opfonts"))

			out(get("ncop3_muxout"))
		}
	}

    // NCOP1v4
	tasks(listOf("NCOP4")) {
        val ncop4_ktemplate by task<Automation> {
            from(get("ncop_ass"))

            video(get("ncop4_premux"))
            script("0x.KaraTemplater.moon")
            macro("0x539's Templater")
            loglevel(Automation.LogLevel.WARNING)
        }

		merge {
			from(ncop4_ktemplate.item())

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
			title(get("ncop4_muxtitle"))

			from(get("ncop4_premux")) {
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

			attach(get("opfonts"))

			out(get("ncop4_muxout"))
		}
	}

    // NCED1
	tasks(listOf("NCED1")) {
        val nced1_ktemplate by task<Automation> {
            from(get("nced_ass"))

            video(get("nced1_premux"))
            script("0x.KaraTemplater.moon")
            macro("0x539's Templater")
            loglevel(Automation.LogLevel.WARNING)
        }

		merge {
			from(nced1_ktemplate.item())

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
			title(get("nced1_muxtitle"))

			from(get("nced1_premux")) {
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

			attach(get("edfonts"))

			out(get("nced1_muxout"))
		}
	}

    // Ep19 NCED
	tasks(listOf("NCEDE19")) {
        val ncede19_ktemplate by task<Automation> {
            from(get("nced19_ass"))

            video(get("ncede19_premux"))
            script("0x.KaraTemplater.moon")
            macro("0x539's Templater")
            loglevel(Automation.LogLevel.WARNING)
        }

		merge {
			from(ncede19_ktemplate.item())

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
			title(get("ncede19_muxtitle"))

			from(get("ncede19_premux")) {
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

			attach(get("ed19fonts"))

			out(get("ncede19_muxout"))
		}
	}
}
