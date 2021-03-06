begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
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
name|index
operator|.
name|query
operator|.
name|AndSource
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
name|IsVisibleToPredicate
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
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_class
DECL|class|AndChangeSource
specifier|public
class|class
name|AndChangeSource
extends|extends
name|AndSource
argument_list|<
name|ChangeData
argument_list|>
implements|implements
name|ChangeDataSource
block|{
DECL|method|AndChangeSource (Collection<Predicate<ChangeData>> that)
specifier|public
name|AndChangeSource
parameter_list|(
name|Collection
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|that
parameter_list|)
block|{
name|super
argument_list|(
name|that
argument_list|)
expr_stmt|;
block|}
DECL|method|AndChangeSource ( Predicate<ChangeData> that, IsVisibleToPredicate<ChangeData> isVisibleToPredicate, int start)
specifier|public
name|AndChangeSource
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|that
parameter_list|,
name|IsVisibleToPredicate
argument_list|<
name|ChangeData
argument_list|>
name|isVisibleToPredicate
parameter_list|,
name|int
name|start
parameter_list|)
block|{
name|super
argument_list|(
name|that
argument_list|,
name|isVisibleToPredicate
argument_list|,
name|start
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasChange ()
specifier|public
name|boolean
name|hasChange
parameter_list|()
block|{
return|return
name|source
operator|!=
literal|null
operator|&&
name|source
operator|instanceof
name|ChangeDataSource
operator|&&
operator|(
operator|(
name|ChangeDataSource
operator|)
name|source
operator|)
operator|.
name|hasChange
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|transformBuffer (List<ChangeData> buffer)
specifier|protected
name|List
argument_list|<
name|ChangeData
argument_list|>
name|transformBuffer
parameter_list|(
name|List
argument_list|<
name|ChangeData
argument_list|>
name|buffer
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasChange
argument_list|()
condition|)
block|{
name|ChangeData
operator|.
name|ensureChangeLoaded
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|transformBuffer
argument_list|(
name|buffer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|compare (Predicate<ChangeData> a, Predicate<ChangeData> b)
specifier|public
name|int
name|compare
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|a
parameter_list|,
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|b
parameter_list|)
block|{
name|int
name|cmp
init|=
name|super
operator|.
name|compare
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|==
literal|0
operator|&&
name|a
operator|instanceof
name|ChangeDataSource
operator|&&
name|b
operator|instanceof
name|ChangeDataSource
condition|)
block|{
name|ChangeDataSource
name|as
init|=
operator|(
name|ChangeDataSource
operator|)
name|a
decl_stmt|;
name|ChangeDataSource
name|bs
init|=
operator|(
name|ChangeDataSource
operator|)
name|b
decl_stmt|;
name|cmp
operator|=
operator|(
name|as
operator|.
name|hasChange
argument_list|()
condition|?
literal|0
else|:
literal|1
operator|)
operator|-
operator|(
name|bs
operator|.
name|hasChange
argument_list|()
condition|?
literal|0
else|:
literal|1
operator|)
expr_stmt|;
block|}
return|return
name|cmp
return|;
block|}
block|}
end_class

end_unit

