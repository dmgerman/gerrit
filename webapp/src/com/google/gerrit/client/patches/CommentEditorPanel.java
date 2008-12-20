begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
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
name|client
operator|.
name|reviewdb
operator|.
name|PatchLineComment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Button
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|ClickListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Composite
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|FlowPanel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|TextArea
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Widget
import|;
end_import

begin_class
DECL|class|CommentEditorPanel
specifier|public
class|class
name|CommentEditorPanel
extends|extends
name|Composite
implements|implements
name|ClickListener
block|{
DECL|field|comment
specifier|private
specifier|final
name|PatchLineComment
name|comment
decl_stmt|;
DECL|field|text
specifier|private
specifier|final
name|TextArea
name|text
decl_stmt|;
DECL|field|save
specifier|private
specifier|final
name|Button
name|save
decl_stmt|;
DECL|field|cancel
specifier|private
specifier|final
name|Button
name|cancel
decl_stmt|;
DECL|method|CommentEditorPanel (final PatchLineComment plc)
specifier|public
name|CommentEditorPanel
parameter_list|(
specifier|final
name|PatchLineComment
name|plc
parameter_list|)
block|{
name|comment
operator|=
name|plc
expr_stmt|;
specifier|final
name|FlowPanel
name|body
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|body
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-CommentEditor"
argument_list|)
expr_stmt|;
name|text
operator|=
operator|new
name|TextArea
argument_list|()
expr_stmt|;
name|text
operator|.
name|setCharacterWidth
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|text
operator|.
name|setVisibleLines
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|buttons
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|buttons
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-CommentEditor-Buttons"
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|buttons
argument_list|)
expr_stmt|;
name|save
operator|=
operator|new
name|Button
argument_list|()
expr_stmt|;
name|save
operator|.
name|setText
argument_list|(
literal|"Save"
argument_list|)
expr_stmt|;
name|save
operator|.
name|addClickListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|save
argument_list|)
expr_stmt|;
name|cancel
operator|=
operator|new
name|Button
argument_list|()
expr_stmt|;
name|cancel
operator|.
name|setText
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
name|cancel
operator|.
name|addClickListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|cancel
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|onClick (Widget sender)
specifier|public
name|void
name|onClick
parameter_list|(
name|Widget
name|sender
parameter_list|)
block|{
if|if
condition|(
name|sender
operator|==
name|save
condition|)
block|{
name|onSave
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sender
operator|==
name|cancel
condition|)
block|{
name|onCancel
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|onSave ()
name|void
name|onSave
parameter_list|()
block|{   }
DECL|method|onCancel ()
name|void
name|onCancel
parameter_list|()
block|{   }
block|}
end_class

end_unit

