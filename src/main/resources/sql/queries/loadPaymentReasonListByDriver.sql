select
    pr.id,
    pr.name,
    pr.payment_type paymentType,
    pr.schedule_type scheduleType
from payment_reasons pr
where
    pr.driver_id = :driverId
