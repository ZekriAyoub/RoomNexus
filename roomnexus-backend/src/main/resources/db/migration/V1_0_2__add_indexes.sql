CREATE INDEX idx_room_company ON room(company_id);
CREATE INDEX idx_user_profile_company ON user_profile(company_id);
CREATE INDEX idx_booking_user ON booking(booked_by);
CREATE INDEX idx_booking_room ON booking(room_id);

CREATE INDEX idx_booking_dates ON booking(room_id, start_time, end_time);