ALTER TABLE `comp421project`.`user` 
ADD COLUMN `info` TEXT NOT NULL;

UPDATE user SET info = '
<profile>
	<name>
		<firstname>a</firstname>
		<lastname>b</lastname>
	</name>
	<age>24</age>
	<sexe>F</sexe>
	<timezone>-5</timezone>
	<description>Hi, my name is a b and I wish to learn how to cook like a wizard.</description>
</profile>
' WHERE id = 1;


UPDATE user SET info = '
<profile>
	<name>
		<firstname>Joe</firstname>
		<lastname>Smith</lastname>
	</name>
	<age>46</age>
	<sexe>F</sexe>
	<timezone>+3</timezone>
	<description>My name is joe and this website looks cool.</description>
</profile>
' WHERE id = 2;

UPDATE user SET info = '
<profile>
	<name>
		<firstname>Wendy</firstname>
		<lastname>Pan</lastname>
	</name>
	<age>7</age>
	<sexe>F</sexe>
	<timezone>+1</timezone>
	<description>Hello world, my name is wendy and I love baking.</description>
</profile>
' WHERE id = 3;

UPDATE user SET info = '
<profile>
	<name>
		<firstname>Timmy</firstname>
		<lastname>Guerin</lastname>
	</name>
	<age>20</age>
	<sexe>M</sexe>
	<timezone>+5</timezone>
	<description>Hey, Im looking for good and easy recipe</description>
</profile>
' WHERE id = 4;

UPDATE user SET info = '
<profile>
	<name>
		<firstname>Vincent</firstname>
		<lastname>Petrella</lastname>
	</name>
	<age>21</age>
	<sexe>M</sexe>
	<timezone>+5</timezone>
	<description>My name is vincent and I am trying to pick up girls on this cool website. Also, I love creme fraiche.</description>
</profile>
' WHERE id = 5;


UPDATE user SET info = '
<profile>
	<name>
		<firstname>Alex</firstname>
		<lastname>Olivier</lastname>
	</name>
	<age>20</age>
	<sexe>M</sexe>
	<timezone>+5</timezone>
	<description>My friend told this website is cool and I love cooking. Looking for cool easy recipe.</description>
</profile>
' WHERE id = 6;

UPDATE user SET info = '
<profile>
	<name>
		<firstname>Francois</firstname>
		<lastname>Hollande</lastname>
	</name>
	<age>72</age>
	<sexe>F</sexe>
	<timezone>+1</timezone>
	<description>Hello my name is Francois and I need help to learn how to cook.</description>
</profile>
' WHERE id = 7;