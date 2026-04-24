INSERT INTO company VALUES
    (null, 'hexagonal', 'gothenburg','ebbe lieberathsgatan', 18, 41283, 'info@hexagonal.com'),
    (null, 'chihuahua', 'stockholm','trekantsvägen', 1, 11743, 'info@rebase.com');

INSERT INTO project VALUES
    (null, 'The Prestige', 'Two friends and fellow magicians become bitter enemies after a sudden tragedy. As they devote themselves to this rivalry, they make sacrifices that bring them fame but, with terrible consequences.','2006-10-20', 'FILM', 'SCI_FI', 'hexagonal', TRUE),
    (null, 'The Illusionist', 'When two lovers are separated owing to their class differences, the boy decides to become a magician. He must use his powers to free his lady from the royal residence.', '2006-08-18', 'FILM', 'ROMANCE', 'hexagonal', TRUE),
    (null, 'Zootopia', 'When Judy Hopps, a rookie officer in the Zootopia Police Department, sniffs out a sinister plot, she enlists the help of a con artist to solve the case in order to prove her abilities to Chief Bogo.','2016-03-16', 'FILM', 'KIDS', 'hexagonal', TRUE),
    (null, 'Star Wars: The Last Jedi', 'Concerned about how strong she is, Luke Skywalker guides Rey as she trains and develops her powers in hopes of assisting the Resistance in their fight against the First Order.', '2017-12-17', 'FILM', 'SCI_FI', 'hexagonal', TRUE),
    (null, 'Me Before You', 'Louisa Clark accepts the job of being a caretaker of Will Traynor, a rigid man who has paralysis. However, her life transforms as the two, gradually, form a bond and later fall in love.', '2016-06-03', 'FILM', 'ROMANCE', 'hexagonal', TRUE),
    (null, 'Wreck It Ralph', 'Ralph is tired of playing the role of a bad guy and embarks on a journey to become a video game hero. But Ralph accidentally lets loose a deadly enemy that threatens the entire arcade.', '2002-11-02', 'FILM', 'KIDS', 'hexagonal', TRUE),
    (null, 'Pokemon', 'A Pokemon master, along with his small group of friends, travels around the world to capture as many pocket monsters as he can.', '1997-04-01', 'SERIES', 'KIDS', 'hexagonal', TRUE),
    (null, 'Howls Moving Castle', 'Jealous of Sophie''s closeness to Howl, a wizard, the Witch of Waste transforms her into an old lady. Sophie must find a way to break the spell with the help of Howl''s friends, Calcifer and Markl.', '2004-11-20', 'FILM', 'FANTASY', 'hexagonal', TRUE),
    (null, 'Arcane', 'The origins of two iconic League champions, set in the utopian Piltover and the oppressed underground of Zaun.', '2021-11-06', 'SERIES', 'ACTION', 'hexagonal', TRUE),
    (null, 'Gladiator', 'Commodus takes over power and demotes Maximus, one of the preferred generals of his father, Emperor Marcus Aurelius. As a result, Maximus is relegated to fighting till death as a gladiator.', '2000-05-19', 'FILM', 'ACTION', 'hexagonal', TRUE),
    (null, 'The Texas Chain Saw Massacre', 'Sally, Franklin and their three friends run out of gas while driving to their grandfather''s house. One by one they are tormented and killed by a chainsaw-wielding killer and his psychopathic family.', '1974-10-11', 'FILM', 'HORROR', 'hexagonal', TRUE),
    (null, 'True Detective', 'Police officers and detectives around the USA are forced to face dark secrets about themselves and the people around them while investigating homicides.', '2014-01-12', 'SERIES', 'CRIME', 'hexagonal', TRUE),
    (null, 'Dexter', 'Dexter Morgan, a man with homicidal tendencies, lives a double life. He works as a forensic technician for the police department during the day and kills heinous perpetrators in his free time.', '2006-10-01', 'SERIES', 'DRAMA', 'hexagonal', TRUE),
    (null, 'The Office', 'The show documents the exploits of a paper supply company in Scranton, Pennsylvania. With an office including the likes of various peers, this series takes a look at the lives of its co-workers.', '2004-03-24', 'SERIES', 'COMEDY', 'hexagonal', TRUE),
    (null, 'Kung Fu Panda', 'When Po the Panda, a kung fu enthusiast, gets selected as the Dragon Warrior, he decides to team up with the Furious Five and destroy the evil forces that threaten the Valley of Peace.', '2008-07-18', 'FILM', 'KIDS', 'chihuahua', TRUE);

INSERT INTO users VALUES
    (null, 'Admin', 'System', 'admin@hexagonal.com', '$2a$12$3UjYpLZtkxLTRTmfzKaq4u9FLA253tB3t1K2XHeCyyjxRV/jcBHO.', 'ADMIN', true, false, '2026-01-01 01:00:00', null  ),
    (null, 'Anna', 'Ziafar', 'anna.ziafar@hexagonal.com', '$2a$12$xx3CDLetuKz.s5YY4uj.ueyFOqID0soxE6kA6QNyB8BPsPzTZGZ66', 'PRODUCER', true, false, '2026-01-01 01:00:00', null  ),
    (null, 'Fredrik', 'Mohlen', 'fredrik.Mohlen@hexagonal.com', '$2a$12$YzLw10lFBu6v1BFYmShlTOuoHoTnPJsh8MNcxOtOovXrT0eXEhC.W', 'RECRUITER', true, false, '2026-01-01 01:00:00', null  ),
    (null, 'Stelle', 'Simonlatser', 'stelle.simonlatser@hexagonal.com', '$2a$12$V59/QnBo9umJdfOZEtKkOuIzJyo5U4D5Qsgwhdw6pdLWyMyXfakIu', 'EDITOR', true, false, '2026-01-01 01:00:00', null  ),
    (null, 'Mikhalis', 'Dragoutas', 'mikhalis.dragoutas@hexagonal.com', '$2a$12$M1VqLXiIBktYao5I.Q/eVeHXlGFXMp0wO4OUBm7DCAA8beQ01ucRi', 'DIRECTOR', true, false, '2026-01-01 01:00:00', null  ),
    (null, 'Eric', 'Phu', 'eric.phu@chihuahua.com', '$2a$12$gbWSoduEuHEFLootTi3DXub1sdDjTB4HQmLvhIYPabL/lUnAzV6mq', 'PRODUCER', true, false, '2026-01-01 01:00:00', null  ),
    (null, 'Bob', 'Brown', 'bob.brown@chihuahua.com', '$2a$12$kXkzk2UvS9I0HItnaeq/quMz/YnwxhOkG/vAp.E6Z8Ts8cGimHpni', 'EDITOR', true, false, '2026-01-01 01:00:00', null  ),
    (null, 'Joe', 'Doe', 'joe.doe@chihuahua.com', '$2a$12$uLLNFxtrWI2y18qqS5ZpSeNGCwpIvmkQHvkRvprhdRXtEdO8s6a5O', 'RECRUITER', true, false, '2026-01-01 01:00:00', null  ),
    (null, 'Jane', 'Smith', 'jane.smith@hexagonal.com', '$2a$12$L7VEd848NY3qwd7wM2Slze4mhQy5sD6ULs966pKisn4ISsY8FJsfe', 'DIRECTOR', true, false, '2026-01-01 01:00:00', null  );

INSERT INTO task VALUES
    (null, 'RECRUITING', 'RECRUIT PEOPLE', '2026-01-01 12:00:00', 'CREATED', 1, 2),
    (null, 'RECORDING', 'RECORD MOVIE', '2026-01-02 12:00:00', 'ASSIGNED', 1, 4);
