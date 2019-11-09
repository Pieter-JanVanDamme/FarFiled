package be.pjvandamme.farfiled.persistence


import be.pjvandamme.farfiled.dao.FaceDao
import be.pjvandamme.farfiled.dao.RelationDao
import be.pjvandamme.farfiled.dao.RelationLifeAreaDao
import be.pjvandamme.farfiled.model.Relation
import be.pjvandamme.farfiled.model.LifeArea
import be.pjvandamme.farfiled.model.RelationLifeArea
import be.pjvandamme.farfiled.TestUtils.getValue
import be.pjvandamme.farfiled.network.NetworkFace
import be.pjvandamme.farfiled.network.asDatabaseModel

import org.junit.Before
import androidx.test.platform.app.InstrumentationRegistry
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FarFiledDatabaseTest {
    private lateinit var faceDao: FaceDao
    private lateinit var relationDao: RelationDao
    private lateinit var relationLifeAreaDao: RelationLifeAreaDao

    private lateinit var database: FarFiledDatabase

    private val relation1 = Relation(
        1L,
        "Pieter-Jan Van Damme",
        "Functioneel Analist voor Colruyt Group en Student Toegepaste Informatica aan de Hogeschool Gent.",
        "https://images-na.ssl-images-amazon.com/images/M/MV5BODM2ODY1NDczNF5BMl5BanBnXkFtZTcwMTI0NDgxNw@@._V1_UX172_CR0,0,172,256_AL_.jpg")

    private val relation2 = Relation(
        2L,
        "Jens Buysse",
        "Lector Informatica aan de Hogeschool Gent.",
        "https://pbs.twimg.com/profile_images/3278700474/d5f5cc2a8835d69d6dde70875a793ce6.jpeg")

    private val relation3 = Relation(
        3L,
        "Harm De Weirdt",
        "Lector Informatica aan de Hogeschool Gent.",
        "https://api.adorable.io/avatars/285/harm.deweirdt@hogent.be")

    private val face1 = NetworkFace(
        "Jason Ehle",
        "jason.ehle@yahoo.com",
        "Delivery Driver",
        "https://images.unsplash.com/photo-1542345812-d98b5cd6cf98?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjE3Nzg0fQ"

    )

    private val face2 = NetworkFace(
        "Pom Klementieff",
        "pom.klementieff@gmail.com",
        "Accountant",
        "https://images-na.ssl-images-amazon.com/images/M/MV5BMTkzNjE5MzY5M15BMl5BanBnXkFtZTgwMDI5ODMxODE@._V1_UY256_CR98,0,172,256_AL_.jpg"
    )

    private val face3 = NetworkFace(
        "Gabriella Horton",
        "gabriella.horton@student.hogent.be",
        "Gabriella Horton",
        "https://images.unsplash.com/photo-1546967191-fdfb13ed6b1e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjE3Nzg0fQ"
    )

    private val face4 = NetworkFace(
        "Jessica Lowndes",
        "j.lowndes@colruytgroup.com",
        "Administrative Assistant",
        "https://images-na.ssl-images-amazon.com/images/M/MV5BOTY2M2I1OTgtYWI5Zi00Mjc1LWI5MDQtNmYzMWM4Y2ExMTNkXkEyXkFqcGdeQXVyMjQwMDg0Ng@@._V1_UX172_CR0,0,172,256_AL_.jpg"
    )

    private val relationLifeArea_1_ephemera = RelationLifeArea(
        1L,
        1L,
        LifeArea.EPHEMERA,
        "Neque porro quisquam est qui.")

    private val relationLifeArea_1_personal = RelationLifeArea(
        2L,
        1L,
        LifeArea.PERSONAL,
        "Dolorem ipsum quia dolor sit amet.")

    private val relationLifeArea_1_vocation = RelationLifeArea(
        3L,
        1L,
        LifeArea.VOCATION,
        "Consectetur, adipisci velit.")

    private val relationLifeArea_1_domestic = RelationLifeArea(
        4L,
        1L,
        LifeArea.DOMESTIC,
        "Duis a enim euismod, blandit libero vitae.")

    private val relationLifeArea_1_community = RelationLifeArea(
        5L,
        1L,
        LifeArea.COMMUNITY,
        "Tincidunt tellus nulla sit amet.")

    private val relationLifeArea_1_leisure = RelationLifeArea(
        6L,
        1L,
        LifeArea.LEISURE,
        "")

    private val relationLifeArea_2_ephemera = RelationLifeArea(
        7L,
        2L,
        LifeArea.EPHEMERA,
        "Neque porro quisquam est qui.")

    private val relationLifeArea_2_personal = RelationLifeArea(
        8L,
        2L,
        LifeArea.PERSONAL,
        "Dolorem ipsum quia dolor sit amet.")

    private val relationLifeArea_2_vocation = RelationLifeArea(
        9L,
        2L,
        LifeArea.VOCATION,
        "Consectetur, adipisci velit.")

    private val relationLifeArea_2_domestic = RelationLifeArea(
        10L,
        2L,
        LifeArea.DOMESTIC,
        "Duis a enim euismod, blandit libero vitae.")

    private val relationLifeArea_2_community = RelationLifeArea(
        11L,
        2L,
        LifeArea.COMMUNITY,
        "Tincidunt tellus nulla sit amet.")

    private val relationLifeArea_2_leisure = RelationLifeArea(
        12L,
        2L,
        LifeArea.LEISURE,
        "")

    private val relationLifeArea_3_ephemera = RelationLifeArea(
        13L,
        3L,
        LifeArea.EPHEMERA,
        "Neque porro quisquam est qui.")

    private val relationLifeArea_3_personal = RelationLifeArea(
        14L,
        3L,
        LifeArea.PERSONAL,
        "Dolorem ipsum quia dolor sit amet.")

    private val relationLifeArea_3_vocation = RelationLifeArea(
        15L,
        3L,
        LifeArea.VOCATION,
        "Consectetur, adipisci velit.")

    private val relationLifeArea_3_domestic = RelationLifeArea(
        16L,
        3L,
        LifeArea.DOMESTIC,
        "Duis a enim euismod, blandit libero vitae.")

    private val relationLifeArea_3_community = RelationLifeArea(
        17L,
        3L,
        LifeArea.COMMUNITY,
        "Tincidunt tellus nulla sit amet.")

    private val relationLifeArea_3_leisure = RelationLifeArea(
        18L,
        3L,
        LifeArea.LEISURE,
        "")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDatabase(){
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room
            .inMemoryDatabaseBuilder(context, FarFiledDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        relationDao = database.relationDao
        faceDao = database.faceDao
        relationLifeAreaDao = database.relationLifeAreaDao

        relationDao.insert(relation1)
        relationDao.insert(relation2)
        relationDao.insert(relation3)

        relationLifeAreaDao.insert(relationLifeArea_1_ephemera)
        relationLifeAreaDao.insert(relationLifeArea_1_personal)
        relationLifeAreaDao.insert(relationLifeArea_1_vocation)
        relationLifeAreaDao.insert(relationLifeArea_1_domestic)
        relationLifeAreaDao.insert(relationLifeArea_1_community)
        relationLifeAreaDao.insert(relationLifeArea_1_leisure)

        relationLifeAreaDao.insert(relationLifeArea_2_ephemera)
        relationLifeAreaDao.insert(relationLifeArea_2_personal)
        relationLifeAreaDao.insert(relationLifeArea_2_vocation)
        relationLifeAreaDao.insert(relationLifeArea_2_domestic)
        relationLifeAreaDao.insert(relationLifeArea_2_community)
        relationLifeAreaDao.insert(relationLifeArea_2_leisure)

        relationLifeAreaDao.insert(relationLifeArea_3_ephemera)
        relationLifeAreaDao.insert(relationLifeArea_3_personal)
        relationLifeAreaDao.insert(relationLifeArea_3_vocation)
        relationLifeAreaDao.insert(relationLifeArea_3_domestic)
        relationLifeAreaDao.insert(relationLifeArea_3_community)
        relationLifeAreaDao.insert(relationLifeArea_3_leisure)

        faceDao.insertAll(*listOf(face1, face2, face3, face4).asDatabaseModel())
    }

    @Test
    fun insertRelations_returnsAllRelations(){
        val relations = getValue(relationDao.getAllRelations())
        assertEquals(3, relations.size)
    }

    @Test
    fun insertFaces_returnsAllFaces(){
        val faces = getValue(faceDao.getAll())
        assertEquals(4, faces.size)
    }

    @Test
    fun insertRelationLifeAreaDaos_returnsAllRelationLifeAreaDaosForRelation(){
        val relationLifeAreas= getValue(relationLifeAreaDao
            .getAllRelationLifeAreasForRelation(relation1.relationId))
        assertEquals(6, relationLifeAreas.size)
    }

    @Test
    fun deleteRelation_deletesRelation(){
        relationDao.delete(relation3)
        val relations = getValue(relationDao.getAllRelations())
        assertEquals(2, relations.size)
    }

    @Test
    fun deleteRelation_alsoDeletesRelationLifeAreas(){
        val relationLifeAreasBefore = getValue(relationLifeAreaDao
            .getAllRelationLifeAreasForRelation(relation2.relationId))
        assertEquals(6, relationLifeAreasBefore.size)

        relationDao.delete(relation2)

        val relationLifeAreasAfter = getValue(relationLifeAreaDao
            .getAllRelationLifeAreasForRelation(relation2.relationId))
        assertEquals(0,relationLifeAreasAfter.size)
    }

    @Test
    fun deleteFace_deletesFace(){
        val face1DataBaseModel = listOf(face1).asDatabaseModel()[0]
        faceDao.delete(face1DataBaseModel)
        val faces = getValue(faceDao.getAll())
        assertEquals(-1, faces.indexOf(listOf(face1).asDatabaseModel()[0]))
    }

    @After
    fun breakDown(){
        database.close()
    }

}