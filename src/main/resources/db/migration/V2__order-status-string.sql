ALTER TABLE public.orders
ALTER COLUMN status TYPE VARCHAR(10);
DELETE FROM public.orders;
