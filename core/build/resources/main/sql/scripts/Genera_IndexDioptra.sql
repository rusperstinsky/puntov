

ALTER TABLE articulos ADD COLUMN indice_dioptra character(15);

update articulos
set indice_dioptra = 
	case when 
	substring(articulo,1,1) in ('P','H','C')
	then
	substring(articulo,1,1) 
	end || ',' ||
	case when 
	substring(articulo,2,1) in ('S','B','P')
	then
	substring(articulo,2,1) 
	end || ',' ||
	case when 
	substring(articulo,3,1) in ('N','P','S')
	then
	substring(articulo,3,1)
	end || ',' ||
	case when 
	substring(articulo,4,2) in ('BL','TG','TB','PG','PB')
	then
	substring(articulo,4,2)
	end || ',' ||
	case when 
	substring(articulo,6,1) in ('E','X','A','B')
	then
	substring(articulo,6,1)
	end || ',' ||
	case when 
	substring(articulo,7,1) in ('T','B')
	then
	substring(articulo,7,1)
	end
where articulo in (
	select articulo 
	from articulos 
	where id_generico = 'B' and articulo  not in ('SV','B','P'))
