begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
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
name|testing
operator|.
name|GerritBaseTests
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
DECL|class|EventTypesTest
specifier|public
class|class
name|EventTypesTest
extends|extends
name|GerritBaseTests
block|{
DECL|class|TestEvent
specifier|public
specifier|static
class|class
name|TestEvent
extends|extends
name|Event
block|{
DECL|field|TYPE
specifier|private
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"test-event"
decl_stmt|;
DECL|method|TestEvent ()
specifier|public
name|TestEvent
parameter_list|()
block|{
name|super
argument_list|(
name|TYPE
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|AnotherTestEvent
specifier|public
specifier|static
class|class
name|AnotherTestEvent
extends|extends
name|Event
block|{
DECL|field|TYPE
specifier|private
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"another-test-event"
decl_stmt|;
DECL|method|AnotherTestEvent ()
specifier|public
name|AnotherTestEvent
parameter_list|()
block|{
name|super
argument_list|(
literal|"another-test-event"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|eventTypeRegistration ()
specifier|public
name|void
name|eventTypeRegistration
parameter_list|()
block|{
name|EventTypes
operator|.
name|register
argument_list|(
name|TestEvent
operator|.
name|TYPE
argument_list|,
name|TestEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|EventTypes
operator|.
name|register
argument_list|(
name|AnotherTestEvent
operator|.
name|TYPE
argument_list|,
name|AnotherTestEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|EventTypes
operator|.
name|getClass
argument_list|(
name|TestEvent
operator|.
name|TYPE
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|TestEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|EventTypes
operator|.
name|getClass
argument_list|(
name|AnotherTestEvent
operator|.
name|TYPE
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|AnotherTestEvent
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getClassForNonExistingType ()
specifier|public
name|void
name|getClassForNonExistingType
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|EventTypes
operator|.
name|getClass
argument_list|(
literal|"does-not-exist-event"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|clazz
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

