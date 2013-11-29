package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Bindable
@ToString
@EqualsAndHashCode
class Plan {
  String id
  String description

  static Plan toPlan( mx.lux.pos.model.Plan plan ) {
    if ( plan?.id ) {
      Plan tmpPlan = new Plan(
          id: plan.id.trim(),
          description: plan.descripcion
      )
      return tmpPlan
    }
    return null
  }
}
