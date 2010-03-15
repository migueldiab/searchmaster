/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.searchmaster.gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author madrax
 */
class lsiTableModel extends DefaultTableModel {
    Class[] types = new Class [] {
      java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
    };
    boolean[] canEdit = new boolean [] {
      true, false, true, false, false
    };

    lsiTableModel(Object[][] data, String[] header) {
      super(data, header);
    }

    public Class getColumnClass(int columnIndex) {
      return types [columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return canEdit [columnIndex];
    }
}
