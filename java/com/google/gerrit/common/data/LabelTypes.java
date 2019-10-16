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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|LabelId
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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

begin_class
DECL|class|LabelTypes
specifier|public
class|class
name|LabelTypes
block|{
DECL|field|labelTypes
specifier|protected
name|List
argument_list|<
name|LabelType
argument_list|>
name|labelTypes
decl_stmt|;
DECL|field|byLabel
specifier|private
specifier|transient
specifier|volatile
name|Map
argument_list|<
name|String
argument_list|,
name|LabelType
argument_list|>
name|byLabel
decl_stmt|;
DECL|field|positions
specifier|private
specifier|transient
specifier|volatile
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|positions
decl_stmt|;
DECL|method|LabelTypes ()
specifier|protected
name|LabelTypes
parameter_list|()
block|{}
DECL|method|LabelTypes (List<? extends LabelType> approvals)
specifier|public
name|LabelTypes
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|LabelType
argument_list|>
name|approvals
parameter_list|)
block|{
name|labelTypes
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|approvals
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getLabelTypes ()
specifier|public
name|List
argument_list|<
name|LabelType
argument_list|>
name|getLabelTypes
parameter_list|()
block|{
return|return
name|labelTypes
return|;
block|}
DECL|method|byLabel (LabelId labelId)
specifier|public
name|LabelType
name|byLabel
parameter_list|(
name|LabelId
name|labelId
parameter_list|)
block|{
return|return
name|byLabel
argument_list|()
operator|.
name|get
argument_list|(
name|labelId
operator|.
name|get
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
DECL|method|byLabel (String labelName)
specifier|public
name|LabelType
name|byLabel
parameter_list|(
name|String
name|labelName
parameter_list|)
block|{
return|return
name|byLabel
argument_list|()
operator|.
name|get
argument_list|(
name|labelName
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
DECL|method|byLabel ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|LabelType
argument_list|>
name|byLabel
parameter_list|()
block|{
if|if
condition|(
name|byLabel
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|byLabel
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|LabelType
argument_list|>
name|l
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|labelTypes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|LabelType
name|t
range|:
name|labelTypes
control|)
block|{
name|l
operator|.
name|put
argument_list|(
name|t
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|byLabel
operator|=
name|l
expr_stmt|;
block|}
block|}
block|}
return|return
name|byLabel
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|labelTypes
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|nameComparator ()
specifier|public
name|Comparator
argument_list|<
name|String
argument_list|>
name|nameComparator
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|positions
init|=
name|positions
argument_list|()
decl_stmt|;
return|return
operator|new
name|Comparator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|left
parameter_list|,
name|String
name|right
parameter_list|)
block|{
name|int
name|lp
init|=
name|position
argument_list|(
name|left
argument_list|)
decl_stmt|;
name|int
name|rp
init|=
name|position
argument_list|(
name|right
argument_list|)
decl_stmt|;
name|int
name|cmp
init|=
name|lp
operator|-
name|rp
decl_stmt|;
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
name|cmp
operator|=
name|left
operator|.
name|compareTo
argument_list|(
name|right
argument_list|)
expr_stmt|;
block|}
return|return
name|cmp
return|;
block|}
specifier|private
name|int
name|position
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Integer
name|p
init|=
name|positions
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|p
operator|!=
literal|null
condition|?
name|p
else|:
name|positions
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|positions ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|positions
parameter_list|()
block|{
if|if
condition|(
name|positions
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|positions
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|p
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|labelTypes
operator|!=
literal|null
condition|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|LabelType
name|t
range|:
name|labelTypes
control|)
block|{
name|p
operator|.
name|put
argument_list|(
name|t
operator|.
name|getName
argument_list|()
argument_list|,
name|i
operator|++
argument_list|)
expr_stmt|;
block|}
block|}
name|positions
operator|=
name|p
expr_stmt|;
block|}
block|}
block|}
return|return
name|positions
return|;
block|}
block|}
end_class

end_unit

