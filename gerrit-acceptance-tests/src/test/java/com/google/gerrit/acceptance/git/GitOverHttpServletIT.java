begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|audit
operator|.
name|AuditEvent
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
name|audit
operator|.
name|AuditService
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
name|FakeAuditService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|AbstractModule
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|CredentialsProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|RefSpec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|UsernamePasswordCredentialsProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_class
DECL|class|GitOverHttpServletIT
specifier|public
class|class
name|GitOverHttpServletIT
extends|extends
name|AbstractPushForReview
block|{
annotation|@
name|Override
DECL|method|beforeTest (Description description)
specifier|protected
name|void
name|beforeTest
parameter_list|(
name|Description
name|description
parameter_list|)
throws|throws
name|Exception
block|{
name|testSysModule
operator|=
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|AuditService
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|FakeAuditService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|super
operator|.
name|beforeTest
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|beforeEach ()
specifier|public
name|void
name|beforeEach
parameter_list|()
throws|throws
name|Exception
block|{
name|CredentialsProvider
operator|.
name|setDefault
argument_list|(
operator|new
name|UsernamePasswordCredentialsProvider
argument_list|(
name|admin
operator|.
name|username
argument_list|,
name|admin
operator|.
name|httpPassword
argument_list|)
argument_list|)
expr_stmt|;
name|selectProtocol
argument_list|(
name|AbstractPushForReview
operator|.
name|Protocol
operator|.
name|HTTP
argument_list|)
expr_stmt|;
name|auditService
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|receivePackAuditEventLog ()
specifier|public
name|void
name|receivePackAuditEventLog
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/for/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
comment|// Git smart protocol makes two requests:
comment|// https://github.com/git/git/blob/master/Documentation/technical/http-protocol.txt
name|assertThat
argument_list|(
name|auditService
operator|.
name|auditEvents
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|AuditEvent
name|e
init|=
name|auditService
operator|.
name|auditEvents
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|who
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|admin
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|what
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"/git-receive-pack"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|params
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|uploadPackAuditEventLog ()
specifier|public
name|void
name|uploadPackAuditEventLog
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|fetch
argument_list|()
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|auditService
operator|.
name|auditEvents
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|AuditEvent
name|e
init|=
name|auditService
operator|.
name|auditEvents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|who
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"ANONYMOUS"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|params
operator|.
name|get
argument_list|(
literal|"service"
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"git-upload-pack"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|what
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"service=git-upload-pack"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

