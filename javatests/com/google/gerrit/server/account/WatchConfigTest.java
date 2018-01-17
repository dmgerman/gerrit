begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
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
name|client
operator|.
name|Account
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
name|client
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
name|server
operator|.
name|account
operator|.
name|ProjectWatches
operator|.
name|NotifyType
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
name|account
operator|.
name|ProjectWatches
operator|.
name|NotifyValue
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
name|account
operator|.
name|ProjectWatches
operator|.
name|ProjectWatchKey
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
name|git
operator|.
name|ValidationError
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|lib
operator|.
name|Config
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
DECL|class|WatchConfigTest
specifier|public
class|class
name|WatchConfigTest
implements|implements
name|ValidationError
operator|.
name|Sink
block|{
DECL|field|validationErrors
specifier|private
name|List
argument_list|<
name|ValidationError
argument_list|>
name|validationErrors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|validationErrors
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseWatchConfig ()
specifier|public
name|void
name|parseWatchConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|fromText
argument_list|(
literal|"[project \"myProject\"]\n"
operator|+
literal|"  notify = * [ALL_COMMENTS, NEW_PATCHSETS]\n"
operator|+
literal|"  notify = branch:master [NEW_CHANGES]\n"
operator|+
literal|"  notify = branch:master [NEW_PATCHSETS]\n"
operator|+
literal|"  notify = branch:foo []\n"
operator|+
literal|"[project \"otherProject\"]\n"
operator|+
literal|"  notify = [NEW_PATCHSETS]\n"
operator|+
literal|"  notify = * [NEW_PATCHSETS, ALL_COMMENTS]\n"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|ProjectWatchKey
argument_list|,
name|ImmutableSet
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|projectWatches
init|=
name|ProjectWatches
operator|.
name|parse
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1000000
argument_list|)
argument_list|,
name|cfg
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|validationErrors
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Project
operator|.
name|NameKey
name|myProject
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"myProject"
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|otherProject
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"otherProject"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|ProjectWatchKey
argument_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|expectedProjectWatches
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedProjectWatches
operator|.
name|put
argument_list|(
name|ProjectWatchKey
operator|.
name|create
argument_list|(
name|myProject
argument_list|,
literal|null
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|,
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
argument_list|)
expr_stmt|;
name|expectedProjectWatches
operator|.
name|put
argument_list|(
name|ProjectWatchKey
operator|.
name|create
argument_list|(
name|myProject
argument_list|,
literal|"branch:master"
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|NEW_CHANGES
argument_list|,
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
argument_list|)
expr_stmt|;
name|expectedProjectWatches
operator|.
name|put
argument_list|(
name|ProjectWatchKey
operator|.
name|create
argument_list|(
name|myProject
argument_list|,
literal|"branch:foo"
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|expectedProjectWatches
operator|.
name|put
argument_list|(
name|ProjectWatchKey
operator|.
name|create
argument_list|(
name|otherProject
argument_list|,
literal|null
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
argument_list|)
expr_stmt|;
name|expectedProjectWatches
operator|.
name|put
argument_list|(
name|ProjectWatchKey
operator|.
name|create
argument_list|(
name|otherProject
argument_list|,
literal|null
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|,
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|projectWatches
argument_list|)
operator|.
name|containsExactlyEntriesIn
argument_list|(
name|expectedProjectWatches
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseInvalidWatchConfig ()
specifier|public
name|void
name|parseInvalidWatchConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|fromText
argument_list|(
literal|"[project \"myProject\"]\n"
operator|+
literal|"  notify = * [ALL_COMMENTS, NEW_PATCHSETS]\n"
operator|+
literal|"  notify = branch:master [INVALID, NEW_CHANGES]\n"
operator|+
literal|"[project \"otherProject\"]\n"
operator|+
literal|"  notify = [NEW_PATCHSETS]\n"
argument_list|)
expr_stmt|;
name|ProjectWatches
operator|.
name|parse
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1000000
argument_list|)
argument_list|,
name|cfg
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|validationErrors
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|validationErrors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"watch.config: Invalid notify type INVALID in project watch of"
operator|+
literal|" account 1000000 for project myProject: branch:master"
operator|+
literal|" [INVALID, NEW_CHANGES]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseNotifyValue ()
specifier|public
name|void
name|parseNotifyValue
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseNotifyValue
argument_list|(
literal|"* []"
argument_list|,
literal|null
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseNotifyValue
argument_list|(
literal|"* [ALL_COMMENTS]"
argument_list|,
literal|null
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseNotifyValue
argument_list|(
literal|"[]"
argument_list|,
literal|null
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseNotifyValue
argument_list|(
literal|"[ALL_COMMENTS, NEW_PATCHSETS]"
argument_list|,
literal|null
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|,
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseNotifyValue
argument_list|(
literal|"branch:master []"
argument_list|,
literal|"branch:master"
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseNotifyValue
argument_list|(
literal|"branch:master || branch:stable []"
argument_list|,
literal|"branch:master || branch:stable"
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseNotifyValue
argument_list|(
literal|"branch:master [ALL_COMMENTS]"
argument_list|,
literal|"branch:master"
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseNotifyValue
argument_list|(
literal|"branch:master [ALL_COMMENTS, NEW_PATCHSETS]"
argument_list|,
literal|"branch:master"
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|,
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseNotifyValue
argument_list|(
literal|"* [ALL]"
argument_list|,
literal|null
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|validationErrors
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseInvalidNotifyValue ()
specifier|public
name|void
name|parseInvalidNotifyValue
parameter_list|()
block|{
name|assertParseNotifyValueFails
argument_list|(
literal|"* [] illegal-characters-at-the-end"
argument_list|)
expr_stmt|;
name|assertParseNotifyValueFails
argument_list|(
literal|"* [INVALID]"
argument_list|)
expr_stmt|;
name|assertParseNotifyValueFails
argument_list|(
literal|"* [ALL_COMMENTS, UNKNOWN]"
argument_list|)
expr_stmt|;
name|assertParseNotifyValueFails
argument_list|(
literal|"* [ALL_COMMENTS NEW_CHANGES]"
argument_list|)
expr_stmt|;
name|assertParseNotifyValueFails
argument_list|(
literal|"* [ALL_COMMENTS, NEW_CHANGES"
argument_list|)
expr_stmt|;
name|assertParseNotifyValueFails
argument_list|(
literal|"* ALL_COMMENTS, NEW_CHANGES]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toNotifyValue ()
specifier|public
name|void
name|toNotifyValue
parameter_list|()
throws|throws
name|Exception
block|{
name|assertToNotifyValue
argument_list|(
literal|null
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
argument_list|,
literal|"* []"
argument_list|)
expr_stmt|;
name|assertToNotifyValue
argument_list|(
literal|"*"
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
argument_list|,
literal|"* []"
argument_list|)
expr_stmt|;
name|assertToNotifyValue
argument_list|(
literal|null
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|)
argument_list|,
literal|"* [ALL_COMMENTS]"
argument_list|)
expr_stmt|;
name|assertToNotifyValue
argument_list|(
literal|"branch:master"
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
argument_list|,
literal|"branch:master []"
argument_list|)
expr_stmt|;
name|assertToNotifyValue
argument_list|(
literal|"branch:master"
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|,
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
argument_list|,
literal|"branch:master [ALL_COMMENTS, NEW_PATCHSETS]"
argument_list|)
expr_stmt|;
name|assertToNotifyValue
argument_list|(
literal|"branch:master"
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ABANDONED_CHANGES
argument_list|,
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|,
name|NotifyType
operator|.
name|NEW_CHANGES
argument_list|,
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|,
name|NotifyType
operator|.
name|SUBMITTED_CHANGES
argument_list|)
argument_list|,
literal|"branch:master [ABANDONED_CHANGES, ALL_COMMENTS, NEW_CHANGES,"
operator|+
literal|" NEW_PATCHSETS, SUBMITTED_CHANGES]"
argument_list|)
expr_stmt|;
name|assertToNotifyValue
argument_list|(
literal|"*"
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|NotifyType
operator|.
name|ALL
argument_list|)
argument_list|,
literal|"* [ALL]"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertParseNotifyValue ( String notifyValue, String expectedFilter, Set<NotifyType> expectedNotifyTypes)
specifier|private
name|void
name|assertParseNotifyValue
parameter_list|(
name|String
name|notifyValue
parameter_list|,
name|String
name|expectedFilter
parameter_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
name|expectedNotifyTypes
parameter_list|)
block|{
name|NotifyValue
name|nv
init|=
name|parseNotifyValue
argument_list|(
name|notifyValue
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|nv
operator|.
name|filter
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedFilter
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|nv
operator|.
name|notifyTypes
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expectedNotifyTypes
argument_list|)
expr_stmt|;
block|}
DECL|method|assertToNotifyValue ( String filter, Set<NotifyType> notifyTypes, String expectedNotifyValue)
specifier|private
specifier|static
name|void
name|assertToNotifyValue
parameter_list|(
name|String
name|filter
parameter_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
name|notifyTypes
parameter_list|,
name|String
name|expectedNotifyValue
parameter_list|)
block|{
name|NotifyValue
name|nv
init|=
name|NotifyValue
operator|.
name|create
argument_list|(
name|filter
argument_list|,
name|notifyTypes
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|nv
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedNotifyValue
argument_list|)
expr_stmt|;
block|}
DECL|method|assertParseNotifyValueFails (String notifyValue)
specifier|private
name|void
name|assertParseNotifyValueFails
parameter_list|(
name|String
name|notifyValue
parameter_list|)
block|{
name|assertThat
argument_list|(
name|validationErrors
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|parseNotifyValue
argument_list|(
name|notifyValue
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|validationErrors
argument_list|)
operator|.
name|named
argument_list|(
literal|"expected validation error for notifyValue: "
operator|+
name|notifyValue
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
name|validationErrors
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|parseNotifyValue (String notifyValue)
specifier|private
name|NotifyValue
name|parseNotifyValue
parameter_list|(
name|String
name|notifyValue
parameter_list|)
block|{
return|return
name|NotifyValue
operator|.
name|parse
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1000000
argument_list|)
argument_list|,
literal|"project"
argument_list|,
name|notifyValue
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|error (ValidationError error)
specifier|public
name|void
name|error
parameter_list|(
name|ValidationError
name|error
parameter_list|)
block|{
name|validationErrors
operator|.
name|add
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

