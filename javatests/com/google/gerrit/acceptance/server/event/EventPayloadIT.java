begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.server.event
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|event
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|GerritConfig
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
name|acceptance
operator|.
name|NoHttpd
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
name|extensions
operator|.
name|events
operator|.
name|RevisionCreatedListener
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|extensions
operator|.
name|registration
operator|.
name|RegistrationHandle
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
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_class
annotation|@
name|NoHttpd
DECL|class|EventPayloadIT
specifier|public
class|class
name|EventPayloadIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|revisionCreatedListeners
annotation|@
name|Inject
specifier|private
name|DynamicSet
argument_list|<
name|RevisionCreatedListener
argument_list|>
name|revisionCreatedListeners
decl_stmt|;
DECL|field|eventListenerRegistration
specifier|private
name|RegistrationHandle
name|eventListenerRegistration
decl_stmt|;
DECL|field|lastEvent
specifier|private
name|RevisionCreatedListener
operator|.
name|Event
name|lastEvent
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|eventListenerRegistration
operator|=
name|revisionCreatedListeners
operator|.
name|add
argument_list|(
literal|"gerrit"
argument_list|,
name|event
lambda|->
name|lastEvent
operator|=
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|cleanup ()
specifier|public
name|void
name|cleanup
parameter_list|()
block|{
name|eventListenerRegistration
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|defaultOptions ()
specifier|public
name|void
name|defaultOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|createChange
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|lastEvent
operator|.
name|getChange
argument_list|()
operator|.
name|submittable
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|lastEvent
operator|.
name|getRevision
argument_list|()
operator|.
name|files
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"event.payload.listChangeOptions"
argument_list|,
name|value
operator|=
literal|"SKIP_MERGEABLE"
argument_list|)
DECL|method|configuredOptions ()
specifier|public
name|void
name|configuredOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|createChange
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|lastEvent
operator|.
name|getChange
argument_list|()
operator|.
name|submittable
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|lastEvent
operator|.
name|getChange
argument_list|()
operator|.
name|mergeable
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|lastEvent
operator|.
name|getRevision
argument_list|()
operator|.
name|files
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|lastEvent
operator|.
name|getChange
argument_list|()
operator|.
name|subject
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

