package jp.dip.oyasirazu.yadome.plugins;

import jp.dip.oyasirazu.yadome.Yadome;
import jp.dip.oyasirazu.yadome.YadomeCellFactory;
import jp.dip.oyasirazu.yadome.YadomeTreeCell;

/**
 * DefaultYadomeCellFactory
 */
public class DefaultYadomeCellFactory implements YadomeCellFactory {
    public YadomeTreeCell newInstance(Yadome yadome) {
        return new DefaultYadomeTreeCell(yadome);
    }
}
