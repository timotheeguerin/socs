function result = improvedeuler(x0,y0,z0,h,N)
x = zeros(N+1, 1);
y = zeros(N+1, 1);
z = zeros(N+1, 1);
x(1) = x0;
y(1) = y0;
z(1) = z0;

for i=1:N
   x(i+1) = xnext(x(i), y(i),z(i), h);
   y(i+1) = ynext(x(i), y(i),z(i), h);
   z(i+1) = znext(x(i), y(i),z(i), h);
end
plot(x,z);
end

function r = fx(x,y,z)
    r = 10*(y-x);
end


function r = fy(x,y,z)
    r = x*(28-z)-y;
end


function r = fz(x,y,z)
    r = x*y-8/3*z;
end

function r = xtild(xi,yi,zi,h)
    r = xi + h*fx(xi,yi,zi);
end

function r = ytild(xi,yi,zi,h)
    r = yi + h*(fy(xi,yi,zi));
end

function r = ztild(xi,yi,zi,h)
    r = zi+h*(fz(xi,yi,zi));
end

function r = xnext(xi,yi,zi,h)
    r = xi + h/2*(fx(xi,yi,zi)+ fx(xtild(xi,yi,zi,h), ytild(xi,yi,zi,h), ztild(xi,yi,zi,h)));
end


function r = ynext(xi,yi,zi,h)
    r = yi + h/2*(fy(xi,yi,zi)+ fy(xtild(xi,yi,zi,h), ytild(xi,yi,zi,h), ztild(xi,yi,zi,h)));
end

function r = znext(xi,yi,zi,h)
    r = zi + h/2*(fz(xi,yi,zi)+ fz(xtild(xi,yi,zi,h), ytild(xi,yi,zi,h), ztild(xi,yi,zi,h)));
end