begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|entities
operator|.
name|Change
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
name|index
operator|.
name|QueryOptions
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
name|index
operator|.
name|Schema
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
name|index
operator|.
name|query
operator|.
name|DataSource
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|index
operator|.
name|query
operator|.
name|QueryParseException
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
name|index
operator|.
name|change
operator|.
name|ChangeIndex
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
name|query
operator|.
name|change
operator|.
name|ChangeData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * This class wraps an index and assumes the search index can't handle any queries. However, it does  * return the current schema as the assumption is that we need a search index for starting Gerrit in  * the first place and only later lose the index connection (making it so that we can't send  * requests there anymore).  */
end_comment

begin_class
DECL|class|DisabledChangeIndex
specifier|public
class|class
name|DisabledChangeIndex
implements|implements
name|ChangeIndex
block|{
DECL|field|index
specifier|private
specifier|final
name|ChangeIndex
name|index
decl_stmt|;
DECL|method|DisabledChangeIndex (ChangeIndex index)
specifier|public
name|DisabledChangeIndex
parameter_list|(
name|ChangeIndex
name|index
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
DECL|method|unwrap ()
specifier|public
name|ChangeIndex
name|unwrap
parameter_list|()
block|{
return|return
name|index
return|;
block|}
annotation|@
name|Override
DECL|method|getSchema ()
specifier|public
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|getSchema
parameter_list|()
block|{
return|return
name|index
operator|.
name|getSchema
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|index
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|replace (ChangeData obj)
specifier|public
name|void
name|replace
parameter_list|(
name|ChangeData
name|obj
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ChangeIndex is disabled"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|delete (Change.Id key)
specifier|public
name|void
name|delete
parameter_list|(
name|Change
operator|.
name|Id
name|key
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ChangeIndex is disabled"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|deleteAll ()
specifier|public
name|void
name|deleteAll
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ChangeIndex is disabled"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getSource (Predicate<ChangeData> p, QueryOptions opts)
specifier|public
name|DataSource
argument_list|<
name|ChangeData
argument_list|>
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ChangeIndex is disabled"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|markReady (boolean ready)
specifier|public
name|void
name|markReady
parameter_list|(
name|boolean
name|ready
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ChangeIndex is disabled"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|get (Change.Id key, QueryOptions opts)
specifier|public
name|Optional
argument_list|<
name|ChangeData
argument_list|>
name|get
parameter_list|(
name|Change
operator|.
name|Id
name|key
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ChangeIndex is disabled"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

