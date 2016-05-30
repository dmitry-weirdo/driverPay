select p from Payment p
where
    p.driver.id = :driverId
    and p.plannedDate < :upTo
    and p.status = :status
    and (p.from.type = :balance
    or p.to.type = :balance)