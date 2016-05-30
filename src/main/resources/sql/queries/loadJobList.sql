select
    j.id,
    j.job_date jobDate,
    j.type,
    j.driver_id driverId,
    d.name driverValue,
    j.pricing,
    sum(jr.net) charging
from jobs j
join drivers d on j.driver_id = d.id
join job_rates jr on j.id = jr.job_id
where
    j.job_date > :from 
    and j.job_date <= :to 
    and (:driver is null or j.driver_id = :driver)
    and (:type is null or j.type = :type)
group by
    j.id,
    j.job_date,
    j.driver_id,
    d.name