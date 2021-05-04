package de.undertrox.orihimemod.document;

import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;
import jp.gr.java_conf.mt777.zukei2d.senbun.Senbun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FoldConverter {
    public static List<List<Double>> toVerticesCoords(Egaki_Syokunin es) {
        int num = es.ori_s.getsousuu();
        List<Senbun> lines = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            lines.add(es.ori_s.get(i));
        }
        List<List<Double>> verticesCoords = lines.parallelStream()
                .flatMap(line ->
                        Stream.of(
                                new Point2d(line.getax(), line.getay()),
                                new Point2d(line.getbx(), line.getby())
                        ))
                .distinct()
                .map(point -> Arrays.asList(point.x, point.y))
                .collect(Collectors.toList());
        return null;
    }
}
