/*
 *  Copyright (C) 2011 Atlas of Living Australia
 *  All Rights Reserved.
 * 
 *  The contents of this file are subject to the Mozilla Public
 *  License Version 1.1 (the "License"); you may not use this file
 *  except in compliance with the License. You may obtain a copy of
 *  the License at http://www.mozilla.org/MPL/
 * 
 *  Software distributed under the License is distributed on an "AS
 *  IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  rights and limitations under the License.
 */

package au.org.ala.volunteer

/**
 * Tag Lib for Transcribe page
 * 
 * @author "Nick dos Remedios <Nick.dosRemedios@csiro.au>"
 */
class TranscribeTagLib {
    /**
     *  Create the field label and form field for the requested
     *  TemplateField type
     *  
     *  @attr templateField REQUIRED
     *  @attr recordValues REQUIRED
     *  @attr recordIdx
     */
    def fieldFromTemplateField = { attrs, body ->
        def field = attrs.templateField
        def recordValues = attrs.recordValues
        def recordIdx = attrs.recordIdx ? Integer.parseInt(attrs.recordIdx) : (int) 0;

        def name = field.fieldType.name()
        //println "TranscribeTagLib: recordValues = " + recordValues
        def label
        if (field.label) {
            label = field.label
        } else {
            label = field.fieldType.label
        }
        def cssClass = name

        if (name =~ /[Dd]ate/) {
            // so we can add a date widget with JQuery
            cssClass = cssClass + ' datePicker';
        }
        if (field.mandatory) {
            cssClass = cssClass + " validate[required]"
        }
        // Uses MarkupBuilder to create HTML
        def mb = new groovy.xml.MarkupBuilder(out)
        def trClass = (field.type == FieldType.hidden) ? 'hidden' : 'prop'
        mb.tr(class:trClass) {
            td(class:'name') {
                if (field.type != FieldType.hidden) {
                    mb.yield(g.message(code:'record.' + name +'.label', default:label))
                }
            }
            td(class:"value") {
                def w // widget
                switch (field.type) {
                    case FieldType.textarea:
                        w = g.textArea(
                            name:"recordValues.${recordIdx}.${name}",
                            rows: ((name == 'occurrenceRemarks') ? 6 : 4),
                            //style: 'width: 100%',
                            value:recordValues?.get(0)?.get(name),
                            'class':cssClass
                        )
                        break
                    case FieldType.hidden:
                        w = g.hiddenField(
                            name:"recordValues.${recordIdx}.${name}",
                            value:recordValues?.get(0)?.get(name),
                            'class':cssClass
                        )
                        break;
                    case FieldType.select:
                        def pl = Picklist.findByName(name)
                        if (pl) {
                            def options = PicklistItem.findAllByPicklist(pl)
                            w = g.select(
                                name:"recordValues.${recordIdx}.${name}",
                                from: options,
                                optionValue:'value',
                                optionKey:'value',
                                value:recordValues?.get(0)?.get(name)?:field?.defaultValue,
                                noSelection:['':''],
                                style: 'max-width: 295px;',
                                'class':cssClass
                            )
                            break
                        } 
                    case FieldType.radio: // fall through TODO
                    case FieldType.checkbox: // fall through TODO
                    case FieldType.text: // fall through
                    case FieldType.autocomplete:
                        cssClass = cssClass + " autocomplete"
                    default:
                        w = g.textField(
                            name:"recordValues.${recordIdx}.${name}",
                            maxLength:200,
                            value:recordValues?.get(0)?.get(name),
                            'class':cssClass
                        )
                }
                mkp.yieldUnescaped(w)

                if (field.helpText) {
                    def help = "<a href='#' class='fieldHelp' title='${field.helpText}'><span class='help-container'>&nbsp;</span></a>"
                    mkp.yieldUnescaped(help)
                }
            }
        }
    }
}