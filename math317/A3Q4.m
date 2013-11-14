function y = solver(u0,v0, h,last,tolerance)
N = last/h;
u = zeros(N+1,1);
v = zeros(N+1,1);
t = zeros(N+1,1);

u(1) = u0;
v(1) = v0;
t(1) = 0;

for i=1:N
    vtmp = v(i)+h*(-2*v(i) -9*u(i));
    v(i+1) = newtonV(vtmp,u(i), v(i),h,tolerance);
    u(i+1) = u(i)+h*v(i+1);
    t(i+1) = t(i) + h;
end
plot(t,u);
end


function r = newtonV(v0,ui,vi,h,tolerance)
r = v0;
residual = 1;
while residual > tolerance
    rold = r;
    r = r - (r-vi-h*(-2*r-9*(ui+h*r)))/(2*h*h+2*h+1);
    residual = abs(r-rold);
end
end