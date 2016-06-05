select
	d.name driver,
	sum(t.net) net,
	sum(t.vat) vat,
	sum(t.total) total
from transactions t
join payments p on t.payment_id = p.id
join drivers d on p.driver_id = d.id
where
	p.payment_document_id in (:paymentDocumentIdList)
group by
	d.name