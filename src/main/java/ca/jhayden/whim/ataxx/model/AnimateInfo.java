package ca.jhayden.whim.ataxx.model;

import java.util.List;

public record AnimateInfo(AtaxxState base, AnimateInfoType type, List<FromToPos> positions) {

}
