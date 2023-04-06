
import styles from "./LinkControl.module.css";

const LinkControl = ({promptForLink, showUrlInput, onUrlChange, urlValue, onLinkInputKeyDown, confirmLink, closeUrlInput}) => {
  return (
    <>
      <button className={styles.button} onMouseDown={promptForLink}>링크추가</button>

      {showUrlInput &&
        <div className={styles.promptForLink}>
          <input
            onChange={onUrlChange}
            type="text"
            value={urlValue}
            onKeyDown={onLinkInputKeyDown}
            className={styles.promptForLinkInputUrl}
            placeholder="https://"
           />
           <button onMouseDown={confirmLink} className={styles.promptForLinkButton}>적용</button>
           <button onMouseDown={closeUrlInput} className={styles.promptForLinkButton}>닫기</button>
        </div>
      }
    </>
  );
};

export default LinkControl;
