begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|AccountGroup
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategoryValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|RefRight
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|SystemConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|workflow
operator|.
name|NoOpFunction
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|workflow
operator|.
name|SubmitFunction
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
operator|.
name|TestDatabase
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|jdbc
operator|.
name|JdbcSchema
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_class
DECL|class|SchemaCreatorTest
specifier|public
class|class
name|SchemaCreatorTest
extends|extends
name|TestCase
block|{
DECL|field|codeReview
specifier|private
name|ApprovalCategory
operator|.
name|Id
name|codeReview
init|=
operator|new
name|ApprovalCategory
operator|.
name|Id
argument_list|(
literal|"CRVW"
argument_list|)
decl_stmt|;
DECL|field|db
specifier|private
name|TestDatabase
name|db
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|db
operator|=
operator|new
name|TestDatabase
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|TestDatabase
operator|.
name|drop
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|testGetCauses_CreateSchema ()
specifier|public
name|void
name|testGetCauses_CreateSchema
parameter_list|()
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
comment|// Initially the schema should be empty.
comment|//
block|{
specifier|final
name|JdbcSchema
name|d
init|=
operator|(
name|JdbcSchema
operator|)
name|db
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|String
index|[]
name|types
init|=
block|{
literal|"TABLE"
block|,
literal|"VIEW"
block|}
decl_stmt|;
specifier|final
name|ResultSet
name|rs
init|=
name|d
operator|.
name|getConnection
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|types
argument_list|)
decl_stmt|;
try|try
block|{
name|assertFalse
argument_list|(
name|rs
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|d
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Create the schema using the current schema version.
comment|//
name|db
operator|.
name|create
argument_list|()
expr_stmt|;
name|db
operator|.
name|assertSchemaVersion
argument_list|()
expr_stmt|;
specifier|final
name|SystemConfig
name|config
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|config
operator|.
name|adminGroupId
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|config
operator|.
name|anonymousGroupId
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|config
operator|.
name|registeredGroupId
argument_list|)
expr_stmt|;
comment|// By default sitePath is set to the current working directory.
comment|//
name|File
name|sitePath
init|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|sitePath
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|sitePath
operator|=
name|sitePath
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|sitePath
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|config
operator|.
name|sitePath
argument_list|)
expr_stmt|;
comment|// This is randomly generated and should be at least 20 bytes long.
comment|//
name|assertNotNull
argument_list|(
name|config
operator|.
name|registerEmailPrivateKey
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|20
operator|<
name|config
operator|.
name|registerEmailPrivateKey
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubsequentGetReads ()
specifier|public
name|void
name|testSubsequentGetReads
parameter_list|()
throws|throws
name|OrmException
block|{
name|db
operator|.
name|create
argument_list|()
expr_stmt|;
specifier|final
name|SystemConfig
name|exp
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
specifier|final
name|SystemConfig
name|act
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|exp
argument_list|,
name|act
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp
operator|.
name|adminGroupId
argument_list|,
name|act
operator|.
name|adminGroupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp
operator|.
name|anonymousGroupId
argument_list|,
name|act
operator|.
name|anonymousGroupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp
operator|.
name|registeredGroupId
argument_list|,
name|act
operator|.
name|registeredGroupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp
operator|.
name|sitePath
argument_list|,
name|act
operator|.
name|sitePath
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp
operator|.
name|registerEmailPrivateKey
argument_list|,
name|act
operator|.
name|registerEmailPrivateKey
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateSchema_Group_Administrators ()
specifier|public
name|void
name|testCreateSchema_Group_Administrators
parameter_list|()
throws|throws
name|OrmException
block|{
name|db
operator|.
name|create
argument_list|()
expr_stmt|;
specifier|final
name|SystemConfig
name|config
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|AccountGroup
name|admin
init|=
name|c
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|config
operator|.
name|adminGroupId
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|adminGroupId
argument_list|,
name|admin
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Administrators"
argument_list|,
name|admin
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|INTERNAL
argument_list|,
name|admin
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testCreateSchema_Group_AnonymousUsers ()
specifier|public
name|void
name|testCreateSchema_Group_AnonymousUsers
parameter_list|()
throws|throws
name|OrmException
block|{
name|db
operator|.
name|create
argument_list|()
expr_stmt|;
specifier|final
name|SystemConfig
name|config
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|AccountGroup
name|anon
init|=
name|c
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|config
operator|.
name|anonymousGroupId
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|anon
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|anonymousGroupId
argument_list|,
name|anon
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Anonymous Users"
argument_list|,
name|anon
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|SYSTEM
argument_list|,
name|anon
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testCreateSchema_Group_RegisteredUsers ()
specifier|public
name|void
name|testCreateSchema_Group_RegisteredUsers
parameter_list|()
throws|throws
name|OrmException
block|{
name|db
operator|.
name|create
argument_list|()
expr_stmt|;
specifier|final
name|SystemConfig
name|config
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|AccountGroup
name|reg
init|=
name|c
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|config
operator|.
name|registeredGroupId
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|registeredGroupId
argument_list|,
name|reg
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Registered Users"
argument_list|,
name|reg
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|SYSTEM
argument_list|,
name|reg
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testCreateSchema_WildCardProject ()
specifier|public
name|void
name|testCreateSchema_WildCardProject
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|create
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|SystemConfig
name|cfg
decl_stmt|;
specifier|final
name|Project
name|all
decl_stmt|;
name|cfg
operator|=
name|c
operator|.
name|systemConfig
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|SystemConfig
operator|.
name|Key
argument_list|()
argument_list|)
expr_stmt|;
name|all
operator|=
name|c
operator|.
name|projects
argument_list|()
operator|.
name|get
argument_list|(
name|cfg
operator|.
name|wildProjectName
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-- All Projects --"
argument_list|,
name|all
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|all
operator|.
name|isUseContributorAgreements
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|all
operator|.
name|isUseSignedOffBy
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testCreateSchema_ApprovalCategory_CodeReview ()
specifier|public
name|void
name|testCreateSchema_ApprovalCategory_CodeReview
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|create
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ApprovalCategory
name|cat
decl_stmt|;
name|cat
operator|=
name|c
operator|.
name|approvalCategories
argument_list|()
operator|.
name|get
argument_list|(
name|codeReview
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|codeReview
argument_list|,
name|cat
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Code Review"
argument_list|,
name|cat
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"R"
argument_list|,
name|cat
operator|.
name|getAbbreviatedName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MaxWithBlock"
argument_list|,
name|cat
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cat
operator|.
name|isCopyMinScore
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cat
operator|.
name|isAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|0
operator|<=
name|cat
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertValueRange
argument_list|(
name|codeReview
argument_list|,
operator|-
literal|2
argument_list|,
operator|-
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateSchema_ApprovalCategory_Read ()
specifier|public
name|void
name|testCreateSchema_ApprovalCategory_Read
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|create
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ApprovalCategory
name|cat
decl_stmt|;
name|cat
operator|=
name|c
operator|.
name|approvalCategories
argument_list|()
operator|.
name|get
argument_list|(
name|ApprovalCategory
operator|.
name|READ
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ApprovalCategory
operator|.
name|READ
argument_list|,
name|cat
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Read Access"
argument_list|,
name|cat
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cat
operator|.
name|getAbbreviatedName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NoOpFunction
operator|.
name|NAME
argument_list|,
name|cat
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cat
operator|.
name|isAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertValueRange
argument_list|(
name|ApprovalCategory
operator|.
name|READ
argument_list|,
operator|-
literal|1
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateSchema_ApprovalCategory_Submit ()
specifier|public
name|void
name|testCreateSchema_ApprovalCategory_Submit
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|create
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ApprovalCategory
name|cat
decl_stmt|;
name|cat
operator|=
name|c
operator|.
name|approvalCategories
argument_list|()
operator|.
name|get
argument_list|(
name|ApprovalCategory
operator|.
name|SUBMIT
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ApprovalCategory
operator|.
name|SUBMIT
argument_list|,
name|cat
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Submit"
argument_list|,
name|cat
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cat
operator|.
name|getAbbreviatedName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SubmitFunction
operator|.
name|NAME
argument_list|,
name|cat
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cat
operator|.
name|isAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertValueRange
argument_list|(
name|ApprovalCategory
operator|.
name|SUBMIT
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateSchema_ApprovalCategory_PushTag ()
specifier|public
name|void
name|testCreateSchema_ApprovalCategory_PushTag
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|create
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ApprovalCategory
name|cat
decl_stmt|;
name|cat
operator|=
name|c
operator|.
name|approvalCategories
argument_list|()
operator|.
name|get
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_TAG
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_TAG
argument_list|,
name|cat
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Push Tag"
argument_list|,
name|cat
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cat
operator|.
name|getAbbreviatedName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NoOpFunction
operator|.
name|NAME
argument_list|,
name|cat
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cat
operator|.
name|isAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertValueRange
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_TAG
argument_list|,
comment|//
name|ApprovalCategory
operator|.
name|PUSH_TAG_SIGNED
argument_list|,
comment|//
name|ApprovalCategory
operator|.
name|PUSH_TAG_ANNOTATED
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateSchema_ApprovalCategory_PushHead ()
specifier|public
name|void
name|testCreateSchema_ApprovalCategory_PushHead
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|create
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ApprovalCategory
name|cat
decl_stmt|;
name|cat
operator|=
name|c
operator|.
name|approvalCategories
argument_list|()
operator|.
name|get
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_HEAD
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_HEAD
argument_list|,
name|cat
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Push Branch"
argument_list|,
name|cat
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cat
operator|.
name|getAbbreviatedName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NoOpFunction
operator|.
name|NAME
argument_list|,
name|cat
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cat
operator|.
name|isAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertValueRange
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_HEAD
argument_list|,
comment|//
name|ApprovalCategory
operator|.
name|PUSH_HEAD_UPDATE
argument_list|,
comment|//
name|ApprovalCategory
operator|.
name|PUSH_HEAD_CREATE
argument_list|,
comment|//
name|ApprovalCategory
operator|.
name|PUSH_HEAD_REPLACE
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateSchema_ApprovalCategory_Owner ()
specifier|public
name|void
name|testCreateSchema_ApprovalCategory_Owner
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|create
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ApprovalCategory
name|cat
decl_stmt|;
name|cat
operator|=
name|c
operator|.
name|approvalCategories
argument_list|()
operator|.
name|get
argument_list|(
name|ApprovalCategory
operator|.
name|OWN
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ApprovalCategory
operator|.
name|OWN
argument_list|,
name|cat
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Owner"
argument_list|,
name|cat
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cat
operator|.
name|getAbbreviatedName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NoOpFunction
operator|.
name|NAME
argument_list|,
name|cat
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cat
operator|.
name|isAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertValueRange
argument_list|(
name|ApprovalCategory
operator|.
name|OWN
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|assertValueRange (ApprovalCategory.Id cat, int... range)
specifier|private
name|void
name|assertValueRange
parameter_list|(
name|ApprovalCategory
operator|.
name|Id
name|cat
parameter_list|,
name|int
modifier|...
name|range
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|HashSet
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|act
init|=
operator|new
name|HashSet
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|ApprovalCategoryValue
name|v
range|:
name|c
operator|.
name|approvalCategoryValues
argument_list|()
operator|.
name|byCategory
argument_list|(
name|cat
argument_list|)
control|)
block|{
name|assertNotNull
argument_list|(
name|v
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|v
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cat
argument_list|,
name|v
operator|.
name|getCategoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|v
operator|.
name|getName
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|act
operator|.
name|add
argument_list|(
name|v
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|value
range|:
name|range
control|)
block|{
specifier|final
name|ApprovalCategoryValue
operator|.
name|Id
name|exp
init|=
operator|new
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|(
name|cat
argument_list|,
operator|(
name|short
operator|)
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|act
operator|.
name|remove
argument_list|(
name|exp
argument_list|)
condition|)
block|{
name|fail
argument_list|(
literal|"Category "
operator|+
name|cat
operator|+
literal|" lacks value "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|act
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Category "
operator|+
name|cat
operator|+
literal|" has additional values: "
operator|+
name|act
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCreateSchema_DefaultAccess_AnonymousUsers ()
specifier|public
name|void
name|testCreateSchema_DefaultAccess_AnonymousUsers
parameter_list|()
throws|throws
name|OrmException
block|{
name|db
operator|.
name|create
argument_list|()
expr_stmt|;
specifier|final
name|SystemConfig
name|config
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
name|assertDefaultRight
argument_list|(
literal|"refs/*"
argument_list|,
name|config
operator|.
name|anonymousGroupId
argument_list|,
name|ApprovalCategory
operator|.
name|READ
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateSchema_DefaultAccess_RegisteredUsers ()
specifier|public
name|void
name|testCreateSchema_DefaultAccess_RegisteredUsers
parameter_list|()
throws|throws
name|OrmException
block|{
name|db
operator|.
name|create
argument_list|()
expr_stmt|;
specifier|final
name|SystemConfig
name|config
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
name|assertDefaultRight
argument_list|(
literal|"refs/*"
argument_list|,
name|config
operator|.
name|registeredGroupId
argument_list|,
name|ApprovalCategory
operator|.
name|READ
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertDefaultRight
argument_list|(
literal|"refs/heads/*"
argument_list|,
name|config
operator|.
name|registeredGroupId
argument_list|,
name|codeReview
argument_list|,
operator|-
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateSchema_DefaultAccess_Administrators ()
specifier|public
name|void
name|testCreateSchema_DefaultAccess_Administrators
parameter_list|()
throws|throws
name|OrmException
block|{
name|db
operator|.
name|create
argument_list|()
expr_stmt|;
specifier|final
name|SystemConfig
name|config
init|=
name|db
operator|.
name|getSystemConfig
argument_list|()
decl_stmt|;
name|assertDefaultRight
argument_list|(
literal|"refs/*"
argument_list|,
name|config
operator|.
name|adminGroupId
argument_list|,
name|ApprovalCategory
operator|.
name|READ
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDefaultRight (final String pattern, final AccountGroup.Id group, final ApprovalCategory.Id category, int min, int max)
specifier|private
name|void
name|assertDefaultRight
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|Id
name|group
parameter_list|,
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|category
parameter_list|,
name|int
name|min
parameter_list|,
name|int
name|max
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|ReviewDb
name|c
init|=
name|db
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|SystemConfig
name|cfg
decl_stmt|;
specifier|final
name|Project
name|all
decl_stmt|;
specifier|final
name|RefRight
name|right
decl_stmt|;
name|cfg
operator|=
name|c
operator|.
name|systemConfig
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|SystemConfig
operator|.
name|Key
argument_list|()
argument_list|)
expr_stmt|;
name|all
operator|=
name|c
operator|.
name|projects
argument_list|()
operator|.
name|get
argument_list|(
name|cfg
operator|.
name|wildProjectName
argument_list|)
expr_stmt|;
name|right
operator|=
name|c
operator|.
name|refRights
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|RefRight
operator|.
name|Key
argument_list|(
name|all
operator|.
name|getNameKey
argument_list|()
argument_list|,
operator|new
name|RefRight
operator|.
name|RefPattern
argument_list|(
name|pattern
argument_list|)
argument_list|,
name|category
argument_list|,
name|group
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|right
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|all
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|right
operator|.
name|getProjectNameKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|group
argument_list|,
name|right
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|category
argument_list|,
name|right
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|min
argument_list|,
name|right
operator|.
name|getMinValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|max
argument_list|,
name|right
operator|.
name|getMaxValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

