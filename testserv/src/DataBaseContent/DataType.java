/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBaseContent;

import DataBaseContent.Generic.DataElement;

/**
 *
 * @author partizanka
 */
public class DataType extends DataElement {
    private String data_type_name_en;
    public DataType(int id, String data_type_name_en) {
        this.id = id;
        this.data_type_name_en=data_type_name_en;
    }
}