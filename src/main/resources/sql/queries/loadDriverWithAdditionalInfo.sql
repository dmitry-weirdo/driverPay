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
),

total_deposit_amount as (
    select
        driver_id,
        sum(gross) total
    from payment_reasons
    where
        payment_type = 'DEPOSIT'
        and driver_id = :id
    group by
        driver_id
),

last_processed_document as (
    select
        driver_id,
        max(payment_date) last_date
    from payment_documents
    where
        processed = 1
        and driver_id = :id
    group by
        driver_id
),

last_calculated_document as (
    select
        driver_id,
        max(payment_date) last_date
    from payment_documents
    where
        processed = 0
        and driver_id = :id
    group by
        driver_id
)

select
	d.id,
	d.name,
	ifnull(csa.total,0.0) calculatedSalary,
	ifnull(cda.total,0.0) currentDeposit,
	ifnull(tda.total,0.0) totalDeposit,
	lpd.last_date lastProcessingDate,
	ifnull(lcd.last_date,lpd.last_date) lastSalaryCalculationDate
from drivers d
left join calculated_salary_amount csa on d.id = csa.driver_id
left join current_deposit_amount cda on d.id = cda.driver_id
left join total_deposit_amount tda on d.id = tda.driver_id
left join last_processed_document lpd on d.id = lpd.driver_id
left join last_calculated_document lcd on d.id = lcd.driver_id
where
    d.id = :id