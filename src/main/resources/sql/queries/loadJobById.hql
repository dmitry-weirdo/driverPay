select j from Job j
left join fetch j.driver
left join fetch j.jobRates
left join fetch j.payment
where j.id = :id