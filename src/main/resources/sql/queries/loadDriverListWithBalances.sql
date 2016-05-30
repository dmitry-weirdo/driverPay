with
calculated_salary_amount as (
	select
        p.driver_id,
        sum(
            case
                when bf.type = 'DRIVER' then -p.total
                when bt.type = 'DRIVER' then p.total
            end
        ) total
	from payments p
	join balances bf on p.from_id = bf.id
	join balances bt on p.to_id = bt.id
	where
	    p.status = 'CALCULATED'
	    and (bf.type = 'DRIVER'
	    or bt.type = 'DRIVER')
	group by
	    p.driver_id
),

current_deposit_amount as (
	select
        p.driver_id,
        sum(
            case
                when bf.type = 'DEPOSIT' then -p.total
                when bt.type = 'DEPOSIT' then p.total
            end
        ) total
	from payments p
	join balances bf on p.from_id = bf.id
    join balances bt on p.to_id = bt.id
	where
	    p.status = 'PROCESSED'
	    and (bf.type = 'DEPOSIT'
        or bt.type = 'DEPOSIT')
	group by
	    p.driver_id
)

select
	d.id,
	d.name,
	ifnull(csa.total,0.0) calculatedSalary,
	ifnull(cda.total,0.0) currentDeposit
from drivers d
left join calculated_salary_amount csa on d.id = csa.driver_id
left join current_deposit_amount cda on d.id = cda.driver_id