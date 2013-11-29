UPDATE pagos SET id_plan = clave_p 
           WHERE id_forma_pago = 'EFD' 
             AND ( id_plan is NULL OR id_plan = '' );
